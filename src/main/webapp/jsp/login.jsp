<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/base.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/auth.css">
</head>

<body>

<div class="auth-card">

    <h2>Login</h2>

    <form action="${pageContext.request.contextPath}/login" method="post">

        <input type="text" name="credential" placeholder="Email or username" required>

        <input type="password" name="password" placeholder="Password" required>

        <button type="submit" class="auth-btn">Login</button>

    </form>

    <a href="${pageContext.request.contextPath}/register" class="auth-link">Register</a>
</div>

</body>
</html>
