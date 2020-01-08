<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>

</head>
<link rel="stylesheet" href="/static/my.css">

<body>
<div class="content-center">
<form class="content-center" action="${pageContext.request.contextPath}/user/login" method="post">
    <a href="/user/register">注册</a><br>
    <label>Name: <input name="name" value="" ></label><br>
    <label>Password: <input name="password" type="password"></label><br>
    <input type="submit" value="登录">
</form>
</div>
</body>
</html>
