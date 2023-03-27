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
    <title>Add into DB</title>
</head>
<body>
<p><a href="/db">На главную</a></p>
<form method="post">
    <label>Table name:
        <input type="text" name="table"><br />
    </label>
    <label>Column 1:
        <input type="text" name="column1"><br />
    </label>
    <label>Column 2:
        <input type="text" name="column2"><br />
    </label>
    <label>Column 3:
        <input type="text" name="column3"><br />
    </label>
    <button type="submit">Submit</button>
</form>
</body>
</html>
