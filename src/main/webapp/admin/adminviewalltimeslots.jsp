<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>View all timeslots</title>
</head>
<body>
<jsp:useBean id="timeslots" scope="request" type="java.util.List"/>
<c:forEach var="entry" items="${timeslots}">
HCP: <c:out value="${entry.healthCareProvider.name}"/>, start time: <c:out value="${entry.startTime}"/>, end time: <c:out value="${entry.endTime}"/><br/>
</c:forEach>
<a href="adminHome"><< Back to home</a> <br/>
</body>
</html>
