<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Preferiti – StudyShare</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/base.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/home.css">
    <style>
        body { background: linear-gradient(135deg, #dbeafe 0%, #fef3c7 100%); min-height: 100vh; }
    </style>
</head>
<body>
<div class="page">
    <div class="topbar">
        <h1>⭐ I Miei Preferiti</h1>
        <div style="display:flex; gap:10px; flex-wrap:wrap;">
            <a href="${pageContext.request.contextPath}/home" class="nav-link">🏠 Home</a>
            <a href="${pageContext.request.contextPath}/upload" class="nav-link">📤 Carica</a>
            <a href="${pageContext.request.contextPath}/profile" class="nav-link">👤 Profilo</a>
        </div>
    </div>

    <%
        List<?> favorites = (List<?>) request.getAttribute("favorites");
        if (favorites == null || favorites.isEmpty()) {
    %>
    <div class="empty-state">
        <p>Non hai ancora aggiunto materiali ai preferiti.</p>
        <a href="${pageContext.request.contextPath}/home" class="nav-link">🔍 Esplora materiali</a>
    </div>
    <%
    } else {
    %>
    <div class="results-grid">
        <%
            for (Object item : favorites) {
        %>
        <div class="card">
            <p><%= item.toString() %></p>
        </div>
        <%
            }
        %>
    </div>
    <%
        }
    %>
</div>
</body>
</html>