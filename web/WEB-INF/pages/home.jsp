<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2020/1/6
  Time: 20:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<link rel="stylesheet" href="/static/my.css">

<body>
<div class="content-center">
    <h4>欢迎登录---${user.name}</h4>
<a href="/user/logout">退出</a><br>
<a href="/user/update">修改</a><br>
<label >Name:${user.name}</label><br>
<label >Sex:${user.sex}</label><br>
<label >Phone:${user.phone}</label><br>
<label >Address:${user.address}</label><br>
<label >Mail:${user.mail}</label><br>
<label >Birthday:${user.birthday}</label><br>

</div>
</body>
</html>
