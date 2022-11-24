<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add Vaccine Record</title>
</head>
<body>
<jsp:useBean id="timeslots" scope="request" type="java.util.List"/>
<c:forEach var="entry" items="${timeslots}">
    <a href="addvaccinerecord?id=<c:out value='${entry.id}'/>">
        Recipient: <c:out value="${entry.recipient.name}"/>, start time: <c:out value="${entry.startTime}"/>, end time: <c:out value="${entry.endTime}"/>
    </a> <br/>
</c:forEach>
<a href="HCPHome"><< Back to home</a> <br/>
</body>
</html>
