<%--
  Created by IntelliJ IDEA.
  User: jwen
  Date: 2020-09-17
  Time: 22:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>SpringBoot整合JSP</title>
</head>
<body>
<table border="1" align="center" width="50%">
    <tr>
        <th>Id</th>
        <th>Name</th>
        <th>Age</th>
    </tr>
    <c:forEach items="${users}" var="user">
        <tr>
            <td>${user.id}</td>
            <td>${user.name}</td>
            <td>${user.age}</td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
