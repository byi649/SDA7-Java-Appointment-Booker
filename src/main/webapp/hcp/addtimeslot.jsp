<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add timeslot</title>
</head>
<body>
<form action="addtimeslot" method="post">
    Start time:<input type="datetime-local" name="start"><br/><br/>
    End time:<input type="datetime-local" name="end"><br/><br/>
    Vaccine type: <input type="text" name="vaccine"/><br/><br/>
    <input type="submit" value="Add"/>
</form>
<jsp:useBean id="message" scope="request" type="java.lang.String"/>
<c:out value="${message}"/> <br/>
<a href="HCPHome"><< Back to home</a> <br/>
</body>
</html>
