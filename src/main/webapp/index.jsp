<%@ page import="org.apache.shiro.subject.Subject" %>
<%@ page import="org.apache.shiro.SecurityUtils" %>
<%@ page import="DatabasePattern.UnitOfWork" %>
<%@ page import="Backend.Account" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>SDA7</title>
</head>
<body>
<p>
    Welcome back,
    <shiro:principal />
</p>
<%
    Subject currentUser = SecurityUtils.getSubject();
    if (currentUser.isAuthenticated()) {
        if (currentUser.hasRole("2")) {
            response.sendRedirect("admin/adminhome.jsp");
            return;
        } else if (currentUser.hasRole("1")) {
            response.sendRedirect("user/userhome.jsp");
            return;
        } else if (currentUser.hasRole("3")) {
            response.sendRedirect("hcp/hcphome.jsp");
            return;
        }
    }
%>
</body>
</html>