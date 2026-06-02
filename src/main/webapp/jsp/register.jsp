<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Register</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/base.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/auth.css">
</head>

<body>

<div class="auth-card">
    <h2>Register</h2>

    <%
        String errorMessage = (String) request.getAttribute("error");
        String nameValue = (String) request.getAttribute("nameValue");
        String usernameValue = (String) request.getAttribute("usernameValue");
        String emailValue = (String) request.getAttribute("emailValue");
        if (errorMessage != null) {
    %>
    <div class="error-box"><%= errorMessage %></div>
    <%
        }
    %>

    <form action="${pageContext.request.contextPath}/register" method="post">

        <input type="text" name="name" placeholder="Name" value="<%= nameValue != null ? nameValue : "" %>" required>

        <input type="text" name="username" placeholder="Username" value="<%= usernameValue != null ? usernameValue : "" %>" required>

        <input type="email" name="email" placeholder="Email" value="<%= emailValue != null ? emailValue : "" %>"
               pattern="^[A-Za-z0-9._%+-]+@studenti\.unipd\.it$" required>
        <div class="helper-text">Use your university email ending with `@studenti.unipd.it`.</div>

        <input type="password" name="password" placeholder="Password"
               pattern="^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).{7,15}$" minlength="7" maxlength="15" required>
        <div class="helper-text">Password must be 7 to 15 characters long and include at least one uppercase letter, one lowercase letter, and one number.</div>

        <button type="submit" class="auth-btn">Register</button>

    </form>

    <a href="${pageContext.request.contextPath}/login" class="auth-link">Already have an account? Log in</a>
</div>

</body>
</html>
