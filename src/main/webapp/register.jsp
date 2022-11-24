<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Create Account</title>
</head>
<body>
<form action="register" method="post">
    Username:<input type="text" name="username"/><br/><br/>
    Password:<input type="password" name="password"/><br/><br/>
    Birthdate (vaccine recipients only):<input type="date" name="birthDate"/><br/><br/>
    <input type="submit" value="Create"/>
</form>
<jsp:useBean id="message" scope="request" type="java.lang.String"/>
<c:out value="${message}"/> <br/>
<a href="index"><< Back to login</a> <br/>
</body>
</html>
