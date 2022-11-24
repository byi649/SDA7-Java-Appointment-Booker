<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Admin Create Account</title>
</head>
<body>
<form action="admincreateaccount" method="post">
    Type:<input type="text" name="type"/><br/><br/>
    Username:<input type="text" name="username"/><br/><br/>
    Password:<input type="password" name="password"/><br/><br/>
    Birthdate (vaccine recipients only):<input type="date" name="birthDate"/><br/><br/>
    Postcode (health care providers only):<input type="text" name="postCode"/><br/><br/>
    <input type="submit" value="Create"/>
</form>
<jsp:useBean id="message" scope="request" type="java.lang.String"/>
<c:out value="${message}"/> <br/>
<a href="adminHome"><< Back to home</a> <br/>
</body>
</html>
