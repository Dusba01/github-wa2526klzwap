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

    <form action="${pageContext.request.contextPath}/register" method="post">

        <input type="text" name="name" placeholder="Nome" required>

        <input type="text" name="username" placeholder="Username" required>

        <input type="email" name="email" placeholder="Email" required>

        <input type="password" name="password" placeholder="Password" required>

        <button type="submit">Registrati</button>

    </form>

    <a href="${pageContext.request.contextPath}/login">Hai un account, accedi <qui></qui></a>
</div>

</body>
</html>