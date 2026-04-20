<!DOCTYPE html>
<html>
<head>
    <title>Register</title>

    <style>
        body {
            margin: 0;
            font-family: Arial, sans-serif;
            background: linear-gradient(135deg, #74ebd5, #9face6);
            height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
        }

        .register-container {
            background: white;
            padding: 30px;
            border-radius: 12px;
            box-shadow: 0 8px 20px rgba(0,0,0,0.2);
            width: 300px;
            text-align: center;
        }

        h2 {
            margin-bottom: 20px;
        }

        .error-box {
            margin-bottom: 14px;
            padding: 10px 12px;
            border-radius: 8px;
            background: #fee2e2;
            color: #991b1b;
            font-size: 14px;
            text-align: left;
        }

        .helper-text {
            margin: 4px 0 10px;
            color: #64748b;
            font-size: 12px;
            text-align: left;
            line-height: 1.45;
        }

        input {
            width: 100%;
            padding: 10px;
            margin: 8px 0;
            border: 1px solid #ccc;
            border-radius: 6px;
            outline: none;
            transition: 0.3s;
        }

        input:focus {
            border-color: #6c63ff;
            box-shadow: 0 0 5px rgba(108,99,255,0.5);
        }

        button {
            width: 100%;
            padding: 10px;
            margin-top: 15px;
            background: #6c63ff;
            color: white;
            border: none;
            border-radius: 6px;
            cursor: pointer;
            font-size: 15px;
            transition: 0.3s;
        }

        button:hover {
            background: #574bdb;
        }

        a {
            display: block;
            margin-top: 15px;
            text-decoration: none;
            color: #4facfe;
        }

        a:hover {
            text-decoration: underline;
        }
    </style>
</head>

<body>

<div class="register-container">
    <h2>Registrazione</h2>

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

        <input type="text" name="name" placeholder="Nome" value="<%= nameValue != null ? nameValue : "" %>" required>

        <input type="text" name="username" placeholder="Username" value="<%= usernameValue != null ? usernameValue : "" %>" required>

        <input type="email" name="email" placeholder="Email" value="<%= emailValue != null ? emailValue : "" %>"
               pattern="^[A-Za-z0-9._%+-]+@studenti\.unipd\.it$" required>
        <div class="helper-text">Use your university email ending with `@studenti.unipd.it`.</div>

        <input type="password" name="password" placeholder="Password"
               pattern="^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).{7,15}$" minlength="7" maxlength="15" required>
        <div class="helper-text">Password must be 7 to 15 characters long and include at least one uppercase letter, one lowercase letter, and one number.</div>

        <button type="submit">Registrati</button>

    </form>

    <a href="${pageContext.request.contextPath}/login">Hai un account, accedi <qui></qui></a>
</div>

</body>
</html>
