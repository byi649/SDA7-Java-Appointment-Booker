<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
  <title>Title</title>
</head>
<body>
<h3>Login Form</h3>
<br/>
<p>
  <%
    if (request.getAttribute("shiroLoginFailure")!=null) {
  %>
  Username or password incorrect
  <%
    }
  %>
</p>
<form name="loginform" action="" method="post">
  Username:<input type="text" name="username"/><br/><br/>
  Password:<input type="password" name="password"/><br/><br/>
  <input type="submit" name="submit" value="Login"/>
</form>
<a href="register">Create an account</a> <br/>
</body>
</html>
