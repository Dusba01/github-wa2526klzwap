<%-- PER REGISTRARE UN NUOVO UTENTE --%>
<%-- PER ORA è UN HTML SEMPLICE --%>

<!DOCTYPE html>
<html>
    <head>
        <title>Register</title>
    </head>
    <body>

    <h2>Registrazione</h2>

    <form action="register" method="post">      <%-- servlet --%>
        Nome: <input type="text" name="name"><br>       <%-- nel backend: request.getParameter("name");  --%>
        Nome: <input type="text" name="username"><br>
        Email: <input type="email" name="email"><br>
        Password: <input type="password" name="password"><br>
        <button type="submit">Registrati</button>
    </form>
    <a href="${pageContext.request.contextPath}/jsp/login.jsp">Already have an account?</a>

    </body>
</html>