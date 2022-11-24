<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add Vaccine Record</title>
</head>
<body>
<form action="addvaccinerecord" method="post">
    <input type="submit" value="Confirm"/>
</form>
<jsp:useBean id="message" scope="request" type="java.lang.String"/>
<c:out value="${message}"/> <br/>
<a href="HCPHome"><< Back to home</a> <br/>
</body>
</html>
