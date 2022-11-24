<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>View vaccination certificate</title>
</head>
<body>
<jsp:useBean id="message" scope="request" type="java.lang.String"/>
<c:out value="${message}"/> <br/>
<a href="userHome"><< Back to home</a> <br/>
</body>
</html>