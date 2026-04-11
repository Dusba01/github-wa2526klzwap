<%-- PER IL LOGIN --%>
<%-- PER ORA è UN HTML SEMPLICE --%>

<!DOCTYPE html>
<html>
    <head>
        <title>Login</title>
    </head>

    <body>
        <h2>Login</h2>

        <form action="${pageContext.request.contextPath}/login" method="post">
            Username or Email: <input type="text" name="credential"><br>
            Password: <input type="password" name="password"><br>       <%-- nel backend: request.getParameter("password");  --%>
            <button type="submit">Login</button>
        </form>

        <a href="${pageContext.request.contextPath}/jsp/register.jsp">Registrati</a>

    </body>
</html>
