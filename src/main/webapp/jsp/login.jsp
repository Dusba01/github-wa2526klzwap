<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
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

    <c:if test="${not empty param.error}">
        <div class="error-box">
            Invalid credentials. Check your username/email and password.
        </div>
    </c:if>

    <c:if test="${not empty param.registered}">
        <div class="success-box">
            Registration completed successfully. Please, log in to access.
        </div>
    </c:if>

    <form action="${pageContext.request.contextPath}/login" method="post">

        <input type="text" name="credential" placeholder="Email or username" required>

        <input type="password" name="password" placeholder="Password" required>

        <button type="submit" class="auth-btn">Login</button>

    </form>

    <a href="${pageContext.request.contextPath}/register" class="auth-link">Register</a>
</div>

</body>
</html>
