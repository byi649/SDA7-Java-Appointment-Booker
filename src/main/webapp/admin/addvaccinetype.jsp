<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add vaccine type</title>
</head>
<body>
<form action="addvaccinetype" method="post">
  Vaccine type:<input type="text" name="type"/><br/><br/>
  <input type="submit" value="Add"/>
</form>
<jsp:useBean id="message" scope="request" type="java.lang.String"/>
<c:out value="${message}"/> <br/>
<a href="adminHome"><< Back to home</a> <br/>
</body>
</html>
