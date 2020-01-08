<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
</head>
<link rel="stylesheet" href="/static/my.css">

<body>
<div class="content-center">
<form class="content-center" action="${pageContext.request.contextPath}/user/register" method="post">
    <label>Name: <input name="name"></label><br>
    <label>Password: <input name="password"></label><br>
    <label>Sex: <input name="sex"></label><br>
    <label>Phone: <input name="phone"></label><br>
    <label>Address: <input name="address"></label><br>
    <label>Mail: <input name="mail"></label><br>
    <label>Birthday: <input name="birthday"></label><br>
    <input type="submit" value="注册">
</form>
</div>
</body>
</html>
