<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registrati – StudyShare</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/base.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/forms.css">
    <style>
        body { background: linear-gradient(135deg, #74ebd5 0%, #9face6 100%); }
    </style>
</head>
<body class="form-page-body">
<div class="form-card">
    <h1>📚 Crea Account</h1>
    <p class="subtitle">Unisciti a StudyShare</p>

    <% String error = (String) request.getAttribute("error"); %>
    <% if (error != null) { %>
    <div class="feedback error"><%= error %></div>
    <% } %>

    <form action="${pageContext.request.contextPath}/register" method="post">
        <div class="form-group">
            <label for="username">Username</label>
            <input type="text" id="username" name="username" placeholder="Scegli un username" required>
        </div>
        <div class="form-group">
            <label for="email">Email</label>
            <input type="email" id="email" name="email" placeholder="La tua email" required>
        </div>
        <div class="form-group">
            <label for="password">Password</label>
            <input type="password" id="password" name="password" placeholder="Crea una password" required>
            <div class="helper-text">Minimo 8 caratteri</div>
        </div>
        <div class="form-group">
            <label for="confirmPassword">Conferma Password</label>
            <input type="password" id="confirmPassword" name="confirmPassword" placeholder="Ripeti la password" required>
        </div>
        <button type="submit" class="btn btn-primary">Registrati</button>
    </form>

    <div class="form-footer">
        Hai già un account? <a href="${pageContext.request.contextPath}/login">Accedi</a>
    </div>
</div>
</body>
</html>