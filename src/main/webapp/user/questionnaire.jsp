<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Questionnaire</title>
</head>
<body>
<jsp:useBean id="message" scope="request" type="java.lang.String"/>
<form action="questionnaire" method="post">
    Question 1: How old are you? <input type="text" name="q1"/><br/><br/>
    Question 2: Do you have any underlying health factors? <input type="text" name="q2"/><br/><br/>
    <input type="submit" value="Submit"/>
</form>
<c:out value="${message}"/> <br/>
<a href="userHome"><< Back to home</a> <br/>
</body>
</html>
