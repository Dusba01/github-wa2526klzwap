<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="model.User" %>
<!DOCTYPE html>

<%
    User user = (User) session.getAttribute("user");

    if (user == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }

    String welcomeName = user.getName();
%>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Home - Notes</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/base.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/layout.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/components.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/home.css">
</head>
<body>

<div class="menu-btn" id="menuBtn" onclick="toggleSidebar()">☰</div>

<div class="sidebar" id="sidebar">
    <h2>Menu</h2>
    <ul>
        <li><a href="${pageContext.request.contextPath}/profile">👤 My uploads/Profile</a></li>
        <li><a href="${pageContext.request.contextPath}/favorites">⭐ Favorites</a></li>
        <li><a href="${pageContext.request.contextPath}/upload-note">📤 Upload notes</a></li>
    </ul>
    <form action="${pageContext.request.contextPath}/logout" method="post" class="logout-form">
        <button type="submit" class="logout-btn">🚪 Logout</button>
    </form>
</div>

<div class="overlay" id="overlay" onclick="toggleSidebar()"></div>

<div class="container">
    <h1>Welcome, <%= welcomeName %>!</h1>

    <h1>Search notes</h1>

    <div class="search-box">
        <input type="text" id="query" placeholder="Search by author, course or content..." required>
        <button type="button" onclick="searchNotes()">🔍</button>
    </div>

    <div id="courseStrip" class="course-strip"></div>

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
<script src="${pageContext.request.contextPath}/js/notes.js"></script>

</body>
</html>
