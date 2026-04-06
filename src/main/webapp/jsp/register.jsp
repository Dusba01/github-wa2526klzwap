<!DOCTYPE html>
<html>
<head>
    <title>Register</title>
    <meta charset="UTF-8">
</head>
<body>

<h2>Registrazione</h2>

<% String error = request.getParameter("error"); %>

<% if ("missing-fields".equals(error)) { %>
<p>Please fill in username, email, and password.</p>
<% } else if ("invalid-email".equals(error)) { %>
<p>Only email addresses ending with @studenti.unipd.it can register.</p>
<% } else if ("weak-password".equals(error)) { %>
<p>Password must contain lowercase, uppercase, number, special character, and be at least 8 characters long.</p>
<% } else if ("user-exists".equals(error)) { %>
<p>A user with the same username or email already exists.</p>
<% } else if ("registration-failed".equals(error)) { %>
<p>Registration failed. Please try again.</p>
<% } %>

<form action="<%= request.getContextPath() %>/register" method="post">
    Username: <input type="text" name="username" required><br>
    Email: <input
            type="email"
            name="email"
            pattern="^[A-Za-z0-9._%+-]+@studenti\.unipd\.it$"
            title="Use your @studenti.unipd.it email address."
            required><br>
    Password: <input
            type="password"
            name="password"
            pattern="^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[^A-Za-z\d]).{8,}$"
            title="At least 8 characters, with lowercase, uppercase, number, and special character."
            required><br>
    <button type="submit">Registrati</button>
</form>

<p>Email must end with <code>@studenti.unipd.it</code>.</p>
<p>Password must include lowercase, uppercase, a number, and a special character.</p>

<p><a href="<%= request.getContextPath() %>/login">Back to login</a></p>

</body>
</html>
