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
            display: flex;
            flex-direction: column;
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
            color: #333;
        }

        .sidebar ul li:hover {
            color: #4f46e5;
        }

        .sidebar ul li a {
            color: inherit;
            text-decoration: none;
            display: block;
            cursor: pointer;
        }

        .logout-form {
            margin-top: auto;
            margin-bottom: 28px;
            display: flex;
            justify-content: center;
        }

        .logout-btn {
            width: 70%;
            border: none;
            border-radius: 10px;
            padding: 12px 16px;
            background: linear-gradient(135deg, #4f46e5, #4338ca);
            color: white;
            font-size: 15px;
            font-weight: 600;
            cursor: pointer;
            box-shadow: 0 6px 14px rgba(79,70,229,0.25);
            transition: transform 0.2s ease, box-shadow 0.2s ease;
        }

        .logout-btn:hover {
            transform: translateY(-1px);
            box-shadow: 0 10px 18px rgba(79,70,229,0.3);
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

        #results {
            margin-top: 20px;
            max-height: 70vh;
            overflow-y: auto;
            padding-right: 10px;
        }

        .card {
            border: 1px solid rgba(99, 102, 241, 0.12);
            border-radius: 18px;
            padding: 18px;
            margin-bottom: 14px;
            background: rgba(255, 255, 255, 0.94);
            box-shadow: 0 14px 34px rgba(15, 23, 42, 0.08);
            backdrop-filter: blur(8px);
        }

        .card h3 {
            margin: 0 0 10px;
            color: #1f2937;
        }

        .card p {
            margin: 0 0 14px;
            color: #475569;
            line-height: 1.5;
        }

        .card-meta {
            display: flex;
            gap: 18px;
            flex-wrap: wrap;
            margin-bottom: 16px;
            color: #475569;
        }

        .card-actions {
            display: flex;
            justify-content: flex-end;
        }

        .download-btn {
            display: inline-flex;
            align-items: center;
            gap: 8px;
            padding: 11px 16px;
            border-radius: 999px;
            background: linear-gradient(135deg, #0f766e, #14b8a6);
            color: #fff;
            text-decoration: none;
            font-weight: 600;
            box-shadow: 0 12px 24px rgba(20, 184, 166, 0.24);
            transition: transform 0.18s ease, box-shadow 0.18s ease;
        }

        .download-btn:hover {
            transform: translateY(-1px);
            box-shadow: 0 16px 30px rgba(20, 184, 166, 0.3);
        }

    </style>
</head>
<body>

<div class="menu-btn" id="menuBtn" onclick="toggleSidebar()">☰</div>

<div class="sidebar" id="sidebar">
    <h2>Menu</h2>
    <ul>
        <li><a href="${pageContext.request.contextPath}/profile">👤 Profilo/Uploads</a></li>
        <li><a href="${pageContext.request.contextPath}/#">🔎 Ricerche recenti</a></li>
        <li><a href="${pageContext.request.contextPath}/#">⭐ Preferiti</a></li>
        <li><a href="${pageContext.request.contextPath}/upload-note">📤 Carica appunti</a></li>
        <li><a href="${pageContext.request.contextPath}/#">⚙️ Impostazioni</a></li>
    </ul>
    <form action="${pageContext.request.contextPath}/logout" method="post" class="logout-form">
        <button type="submit" class="logout-btn">🚪 Logout</button>
    </form>
</div>

<div class="overlay" id="overlay" onclick="toggleSidebar()"></div>

<div class="container">
    <h1>Cerca Appunti</h1>

    <div class="search-box">
        <input type="text" id="query" placeholder="Cerca per autore, corso o contenuto..." required>
        <button type="button" onclick="searchNotes()">🔍</button>
    </div>

    <div id="results"></div>
</div>

<script>
    function toggleSidebar() {
        document.getElementById("sidebar").classList.toggle("active");
        document.getElementById("overlay").classList.toggle("active");
        document.getElementById("menuBtn").classList.toggle("active");
    }
</script>

<script>
    const BASE_URL = "${pageContext.request.contextPath}";
</script>
<script src="${pageContext.request.contextPath}/js/main.js"></script>

</body>
</html>
