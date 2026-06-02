<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login – StudyShare</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/base.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/forms.css">
    <style>
        body { background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%); }
    </style>
</head>
<body class="form-page-body">
<div class="form-card">
    <h1>👋 Bentornato</h1>
    <p class="subtitle">Accedi al tuo account StudyShare</p>

    <% String error = (String) request.getAttribute("error"); %>
    <% String success = (String) request.getAttribute("success"); %>
    <% if (error != null) { %>
    <div class="feedback error"><%= error %></div>
    <% } %>
    <% if (success != null) { %>
    <div class="feedback success"><%= success %></div>
    <% } %>

    <form action="${pageContext.request.contextPath}/login" method="post">
        <div class="form-group">
            <label for="username">Username</label>
            <input type="text" id="username" name="username" placeholder="Il tuo username" required>
        </div>
        <div class="form-group">
            <label for="password">Password</label>
            <input type="password" id="password" name="password" placeholder="La tua password" required>
        </div>
        <button type="submit" class="btn btn-primary">Accedi</button>
    </form>

    <div class="form-footer">
        Non hai un account? <a href="${pageContext.request.contextPath}/register">Registrati</a>
    </div>
</div>
</body>
</html>