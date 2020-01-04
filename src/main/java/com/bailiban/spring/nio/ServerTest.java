package com.bailiban.spring.nio;
import com.bailiban.spring.bs.HttpServer;
import com.bailiban.spring.demo.MyRequestMapping;
import com.bailiban.spring.demo.MyRestController;

import java.io.File;
import java.io.IOException;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.InetSocketAddress;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;

public class ServerTest {
    //IOC容器
    public static Map<String,Object> beanMap = new HashMap<>();
    //URL映射，RequestMapping
    public static Map<String,MethodInfo> methodMap = new HashMap<>();

    public static void main(String[] args) throws IOException {
        refreshBeanFactory("com.bailiban.spring.demo");
        //------
        //建立服务器端socket
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(80));
        serverSocketChannel.configureBlocking(false);//非阻塞
        //建立selector
        Selector selector = Selector.open();
        //将服务器端socket注册到selector
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("Http Server Started");
        while (true) {
            if (selector.select(3000) <= 0) {
                continue;
            }
            Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
            while (keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next();
                HttpHandle(key);
                keyIterator.remove();
            }
        }
    }
    // 1. 初始化 beanMap
    // 2. 初始化 methodMap
    private static void refreshBeanFactory(String pkg) throws UnsupportedEncodingException {
        //将包名中的点转换为斜杠
        String path = pkg.replace(".", "/");
        //从class path中获取上述资源
        URL url = ServerTest.class.getClassLoader().getResource(path);
        //获取包名中对应的文件夹
        File rootPkgDir = new File(URLDecoder.decode(url.getPath(), "utf-8"));
        //遍历文件夹，解析class
        beanParse(rootPkgDir);
    }

    private static void beanParse(File dir) {
        //非目录直接返回
        if (!dir.isDirectory()){
            return;
        }
        //过滤并获取目录下的所有文件
        File[] files = dir.listFiles(pathname -> {
            // 如果是目录，则递归调用 beanParse，并且返回false
            if (pathname.isDirectory()) {
                beanParse(pathname);
                return false;
            }
            // 对应文件，则判断是否为class文件 ，只处理class文件
            return pathname.getName().endsWith(".class");
        });
        for (File f:files){
            //获取绝对路径
            String filePath = f.getAbsolutePath();
            //获取全类名：包名+类名
            String className = filePath.split("classes\\\\")[1].replace("\\",".").split("\\.class")[0];
            try {
                Class<?> cls = Class.forName(className);

                MyRestController myRestController = cls.getAnnotation(MyRestController.class);
               //处理MyRestController注解的类
                if (myRestController != null){
                    controllerParse(cls);
                }

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
    //处理MyRestController注解的类
    private static void controllerParse(Class<?> cls) {
        try {
            //IOC容器注入
            beanMap.put(cls.getSimpleName(),cls.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        //解析 MyRequestMapping注解的方法，初始化methodMap
        Method[] methods = cls.getDeclaredMethods();
        for (Method method:methods){
            MyRequestMapping myRequestMapping = method.getDeclaredAnnotation(MyRequestMapping.class);
            if (myRequestMapping == null)
                continue;
            String url =myRequestMapping.value();
            methodMap.put(url,new MethodInfo(method,cls.getSimpleName()));

        }

    }

    private static void HttpHandle(SelectionKey key) throws IOException {
        if (key.isAcceptable()) {
            AcceptHandle(key);
        } else if (key.isReadable()) {
            RequestHandle(key);
        }
    }

    private static void AcceptHandle(SelectionKey key) throws IOException {
        SocketChannel socketChannel = ((ServerSocketChannel) key.channel()).accept();
        socketChannel.configureBlocking(false);
        socketChannel.register(key.selector(), SelectionKey.OP_READ, ByteBuffer.allocate(1024));

    }

    private static void RequestHandle(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        ByteBuffer byteBuffer = (ByteBuffer) key.attachment();
        //clear:设置为读模式
        byteBuffer.clear();
        if (socketChannel.read(byteBuffer) == -1) {
            socketChannel.close();
            return;
        }
        //设置读模式
        byteBuffer.flip();
        String requestMsg = new String(byteBuffer.array());
        String url = requestMsg.split("\r\n")[0].split(" ")[1];

        List<String> urlParams = new ArrayList<>();
        urlParamsParse(url,urlParams);
        url = url.split("\\?")[0];
        MethodInfo methodInfo = methodMap.get(url);
        String content = null;

        try {
            content = methodInvoke(url, urlParams);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        if (content == null){
            content="404";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("HTTP/1.1 200 OK\r\n");
        stringBuilder.append("Content-Type:text/html;charset=utf-8\r\n");
        stringBuilder.append("\r\n");
        stringBuilder.append("<html><head><title>HttpTest</title></head><body>");
        stringBuilder.append(content);
        stringBuilder.append("</body></html>");

        socketChannel.write(ByteBuffer.wrap(stringBuilder.toString().getBytes()));
        socketChannel.close();

    }

    private static String methodInvoke(String url, List<String> urlParams) throws InvocationTargetException, IllegalAccessException {
        MethodInfo methodInfo =methodMap.get(url);
        if (methodInfo == null) {
            return "404";
        }
        String className = methodInfo.getClassName();
        Method method = methodInfo.getMethod();
        Object beanObj = beanMap.get(className);
        Object[] params = new Object[urlParams.size()];
        Parameter[] parameters = method.getParameters();
        if (params.length != parameters.length){
            return "参数个数不匹配";
        }
        int i = 0;
        for (Parameter p:parameters){
            String type = p.getType().getSimpleName();
            String pName = p.getName();
            boolean flag = false;
            for (String p2:urlParams){
                String pp[] = p2.split("=");
                if (pName.equals(pp[0].trim())){
                    //根据类型进行参数转换
                    Object pValue = paramTranslate(type,pp[1]);
                    params[i++] = pValue;
                    flag = true;
                    continue;
                }
            }
            if (!flag)
                return "参数名称不匹配";
        }
        return (String) method.invoke(beanObj, params);

    }

    private static Object paramTranslate(String type, String s) {
        switch (type) {
            case "int":
                return Integer.valueOf(s);
            case "double":
                return Double.valueOf(s);
            case "float":
                return Float.valueOf(s);
            default:
                return s;
        }
    }


    //解析url参数
    private static void urlParamsParse(String url, List<String> urlParams) {
        if (!url.contains("?")){
            return;
        }
        String[] ps = url.replaceFirst(".*?\\?","").split("&");
        for (String p :ps){
            if (!p.contains("=="))
                continue;
                urlParams.add(p);

        }
    }

}

