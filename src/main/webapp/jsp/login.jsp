<!DOCTYPE html>
<html>
<head>
    <title>Login</title>
    <meta charset="UTF-8">
</head>
<body>

<h2>Login</h2>

<% String error = request.getParameter("error"); %>
<% String registered = request.getParameter("registered"); %>

<% if ("missing-credentials".equals(error)) { %>
<p>Please enter your username or email and password.</p>
<% } else if ("invalid-credentials".equals(error)) { %>
<p>Invalid username/email or password.</p>
<% } %>

<% if ("true".equals(registered)) { %>
<p>Registration completed. You can now log in.</p>
<% } %>

<form action="<%= request.getContextPath() %>/login" method="post">
    Username or Email: <input type="text" name="usernameOrEmail"><br>
    Password: <input type="password" name="password"><br>
    <button type="submit">Login</button>
</form>

<p><a href="<%= request.getContextPath() %>/register">Create an account</a></p>

</body>
</html>
