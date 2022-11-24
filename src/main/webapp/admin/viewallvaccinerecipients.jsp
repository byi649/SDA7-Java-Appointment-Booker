<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>View all vaccine recipients</title>
</head>
<body>
<jsp:useBean id="accounts" scope="request" type="java.util.List"/>
<form action="viewallvaccinerecipients" method="post">
    Vaccine:<input type="text" name="vaccine"/><br/><br/>
    <input type="submit" value="Search"/>
</form>
<c:forEach var="entry" items="${accounts}">
    <c:out value="${entry.name}"/> <br/>
</c:forEach>
<a href="adminHome"><< Back to home</a> <br/>
</body>
</html>
