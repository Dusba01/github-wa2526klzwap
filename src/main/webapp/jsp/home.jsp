<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Home – StudyShare</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/base.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/home.css">
</head>
<body class="home-body">

<!-- Overlay sidebar -->
<div class="overlay" id="overlay" onclick="closeSidebar()"></div>

<!-- Sidebar -->
<div class="sidebar" id="sidebar">
    <div class="sidebar-title">📚 StudyShare</div>
    <a href="${pageContext.request.contextPath}/home">🏠 Home</a>
    <a href="${pageContext.request.contextPath}/upload">📤 Carica</a>
    <a href="${pageContext.request.contextPath}/favorites">⭐ Preferiti</a>
    <a href="${pageContext.request.contextPath}/profile">👤 Profilo</a>
</div>

<!-- Topbar -->
<div class="home-topbar">
    <button class="menu-btn" onclick="toggleSidebar()">☰</button>
    <h1>📚 StudyShare</h1>
    <div style="display:flex; gap:10px; align-items:center;">
        <a href="${pageContext.request.contextPath}/upload" class="nav-link">📤 Carica</a>
        <a href="${pageContext.request.contextPath}/favorites" class="nav-link">⭐ Preferiti</a>
        <a href="${pageContext.request.contextPath}/profile" class="nav-link">👤 Profilo</a>
        <form action="${pageContext.request.contextPath}/logout" method="post" class="logout-form">
            <button type="submit" class="logout-btn">Esci</button>
        </form>
    </div>
</div>

<!-- Contenuto principale -->
<div class="main-content">

    <!-- Search box -->
    <div class="search-box">
        <h2>🔍 Cerca Materiale</h2>
        <form action="${pageContext.request.contextPath}/home" method="get">
            <div class="search-row">
                <input type="text" name="query" placeholder="Titolo, descrizione..."
                       value="${param.query != null ? param.query : ''}">
                <input type="text" name="course" placeholder="Corso..."
                       value="${param.course != null ? param.course : ''}">
                <input type="text" name="subject" placeholder="Materia..."
                       value="${param.subject != null ? param.subject : ''}">
                <button type="submit" class="search-btn">🔍 Cerca</button>
            </div>
        </form>
    </div>

    <!-- Course strip -->
    <div class="course-strip" id="courseStrip"></div>

    <!-- Risultati -->
    <div class="results-grid" id="resultsGrid"></div>

    <!-- Empty state -->
    <div class="empty-state" id="emptyState" style="display:none;">
        <p>😕 Nessun materiale trovato.</p>
        <a href="${pageContext.request.contextPath}/upload" class="btn btn-primary" style="width:auto;">
            📤 Carica il primo
        </a>
    </div>

</div>

<script src="${pageContext.request.contextPath}/js/main.js"></script>
<script>
    function toggleSidebar() {
        document.getElementById('sidebar').classList.toggle('active');
        document.getElementById('overlay').classList.toggle('active');
    }
    function closeSidebar() {
        document.getElementById('sidebar').classList.remove('active');
        document.getElementById('overlay').classList.remove('active');
    }
</script>
</body>
</html>