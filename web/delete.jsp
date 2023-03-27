<%--
  Created by IntelliJ IDEA.
  User: ap83
  Date: 21.12.2022
  Time: 19:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Delete from DB</title>
</head>
<body>
<p><a href="/db">На главную</a></p>
<form method="post">
    <label>Table name:
        <input type="text" name="table"><br />
    </label>
    <label>ID:
        <input type="text" name="id"><br />
    </label>
    <button type="submit">Delete</button>
</form>
</body>
</html>
