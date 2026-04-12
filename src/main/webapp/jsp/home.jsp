<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Home - Appunti</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <style>
        body {
            margin: 0;
            font-family: 'Inter', sans-serif;
            background: linear-gradient(135deg, #74ebd5, #9face6);
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }

        .container {
            text-align: center;
            width: 100%;
            max-width: 600px;
        }

        h1 {
            margin-bottom: 30px;
            color: #333;
        }

        .search-box {
            display: flex;
            border-radius: 12px;
            overflow: hidden;
            box-shadow: 0 4px 12px rgba(0,0,0,0.1);
            background: white;
        }

        .search-box input {
            flex: 1;
            padding: 15px;
            border: none;
            outline: none;
            font-size: 16px;
        }

        .search-box button {
            padding: 0 20px;
            border: none;
            background-color: #4f46e5;
            color: white;
            cursor: pointer;
            font-size: 16px;
        }

        .search-box button:hover {
            background-color: #4338ca;
        }

        /* Sidebar */
        .sidebar {
            position: fixed;
            top: 0;
            left: -260px;
            width: 260px;
            height: 100%;
            background: #ffffff;
            box-shadow: 2px 0 10px rgba(0,0,0,0.1);
            padding: 20px;
            transition: left 0.3s ease;
            z-index: 1000;
        }

        .sidebar.active {
            left: 0;
        }

        .sidebar h2 {
            margin-top: 0;
        }

        .sidebar ul {
            list-style: none;
            padding: 0;
        }

        .sidebar ul li {
            margin: 15px 0;
            cursor: pointer;
            color: #333;
        }

        .sidebar ul li:hover {
            color: #4f46e5;
        }

        /* Toggle button */
        .menu-btn {
            position: fixed;
            top: 20px;
            left: 20px;
            font-size: 24px;
            cursor: pointer;
            background: white;
            border-radius: 8px;
            padding: 8px 12px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
            transition: left 0.3s ease;
            z-index: 1100;
        }

        /* Quando sidebar aperta, il bottone si sposta */
        .menu-btn.active {
            left: 280px;
        }

        /* Overlay */
        .overlay {
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: rgba(0,0,0,0.3);
            display: none;
            z-index: 900;
        }

        .overlay.active {
            display: block;
        }

    </style>
</head>
<body>

<div class="menu-btn" id="menuBtn" onclick="toggleSidebar()">☰</div>

<div class="sidebar" id="sidebar">
    <h2>Menu</h2>
    <ul>
        <li>👤 Profilo</li>
        <li>🔎 Ricerche recenti</li>
        <li>⭐ Preferiti</li>
        <li>📤 Carica appunti</li>
        <li>⚙️ Impostazioni</li>
    </ul>
</div>

<div class="overlay" id="overlay" onclick="toggleSidebar()"></div>

<div class="container">
    <h1>Cerca Appunti</h1>

    <form action="search.jsp" method="get" class="search-box">
        <input type="text" name="query" placeholder="Cerca corsi, appunti, università..." required>
        <button type="submit">🔍</button>
    </form>
</div>

<script>
    function toggleSidebar() {
        document.getElementById("sidebar").classList.toggle("active");
        document.getElementById("overlay").classList.toggle("active");
        document.getElementById("menuBtn").classList.toggle("active");
    }
</script>

</body>
</html>
