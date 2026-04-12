<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
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

        .card {
            background: white;
            padding: 30px;
            border-radius: 12px;
            width: 320px;
            box-shadow: 0 10px 25px rgba(0,0,0,0.2);
        }

        h2 {
            text-align: center;
            margin-bottom: 20px;
            color: #333;
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

        .link {
            text-align: center;
            margin-top: 12px;
            font-size: 13px;
        }

        .link a {
            color: #6c63ff;
            text-decoration: none;
        }

        .link a:hover {
            text-decoration: underline;
        }
    </style>
</head>

<body>

<div class="card">

    <h2>Registrazione</h2>

    <form action="register" method="post">

        <input type="text" name="name" placeholder="Nome" required>

        <input type="email" name="email" placeholder="Email" required>

        <input type="password" name="password" placeholder="Password" required>

        <button type="submit">Registrati</button>

    </form>

    <div class="link">
        Hai un account?
        <a href="login.jsp">Accedi</a>
    </div>

</div>

</body>
</html>