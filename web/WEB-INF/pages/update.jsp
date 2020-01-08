<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
</head>
<link rel="stylesheet" href="/static/my.css">
<body>
<div class="content-center">
<form class="content-center" action="${pageContext.request.contextPath}/user/update" method="post">
    <label hidden>Name: <input name="id" value="${user.id}" ></label><br>
    <label>Name: <input name="name" value="${user.name}" ></label><br>
    <label>Password: <input name="password" value="${user.password}" type="password"></label><br>
    <label>Sex: <input name="sex" value="${user.sex}" ></label><br>
    <label>Phone: <input name="phone" value="${user.phone}" ></label><br>
    <label>Address: <input name="address" value="${user.address}" ></label><br>
    <label>Mail: <input name="mail" value="${user.mail}" ></label><br>
    <label>Birthday: <input name="birthday" value="${user.birthday}" ></label><br>
    <input type="submit" value="修改并保存">
</form>
</div>
</body>
</html>
