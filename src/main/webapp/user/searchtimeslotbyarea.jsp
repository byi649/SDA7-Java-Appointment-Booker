<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Search timeslot by area</title>
</head>
<body>
<form action="searchtimeslotbyarea" method="post">
    Area postcode:<input type="text" name="Postcode"/><br/><br/>
    <input type="submit" value="Search"/>
</form>
<jsp:useBean id="timeslots" scope="request" type="java.util.List"/>
<c:forEach var="entry" items="${timeslots}">
    <a href="questionnaire?timeslot=<c:out value='${entry.id}'/>">
        HCP: <c:out value="${entry.healthCareProvider.name}"/>, start time: <c:out value="${entry.startTime}"/>, end time: <c:out value="${entry.endTime}"/>
    </a> <br/>
</c:forEach>
<a href="userHome"><< Back to home</a> <br/>
</body>
</html>
