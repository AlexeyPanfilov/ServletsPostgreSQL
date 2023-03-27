<%--
  Created by IntelliJ IDEA.
  User: ap83
  Date: 18.12.2022
  Time: 18:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
</head>
<body>
<h1>Database page</h1>
<p><a href="${pageContext.request.contextPath}/db/banks">Список банков</a></p>
<p><a href="${pageContext.request.contextPath}/db/persons">Список людей</a></p>
<p><a href="${pageContext.request.contextPath}/db/persons_cards">Список карт по клиентам</a></p>
<p><a href="${pageContext.request.contextPath}/db/cards">Информация по картам</a></p>
<p><a href="${pageContext.request.contextPath}/db/banks_persons">Список клиентов банков</a></p>
<p><a href="${pageContext.request.contextPath}/db/banks_cards">Список банков и выпущенных ими карт</a></p>
<p><a href="${pageContext.request.contextPath}/db/add">Добавить сущность</a></p>
<p><a href="${pageContext.request.contextPath}/db/update">Изменить сущность</a></p>
<p><a href="${pageContext.request.contextPath}/db/delete">Удалить сущность</a></p>
</body>
</html>
