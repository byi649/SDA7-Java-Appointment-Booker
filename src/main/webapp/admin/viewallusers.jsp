<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>View all users</title>
</head>
<body>
<jsp:useBean id="accounts" scope="request" type="java.util.List"/>
<c:forEach var="entry" items="${accounts}">
    <c:out value="${entry}"/> <br/>
</c:forEach>
<a href="adminHome"><< Back to home</a> <br/>
</body>
</html>
