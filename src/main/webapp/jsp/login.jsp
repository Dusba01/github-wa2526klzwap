<%-- PER IL LOGIN --%>
<%-- PER ORA è UN HTML SEMPLICE --%>

<!DOCTYPE html>
<html>
    <head>
        <title>Login</title>
    </head>

    <body>
        <h2>Login</h2>

        <form action="login" method="post">   <%-- servlet --%>
            Email: <input type="email" name="email"><br>     <%-- nel backend: request.getParameter("email");  --%>
            Password: <input type="password" name="password"><br>       <%-- nel backend: request.getParameter("password");  --%>
            <button type="submit">Login</button>
        </form>

        <a href="register.jsp">Registrati</a>

    </body>
</html>

