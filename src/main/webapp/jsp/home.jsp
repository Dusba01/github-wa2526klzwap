<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${empty sessionScope.user}">
    <c:redirect url="/login"/>
</c:if>

<!DOCTYPE html>
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

<%-- Hamburger: visibile solo su mobile (nascosto su desktop via CSS) --%>
<button class="menu-btn" id="menuBtn" onclick="toggleSidebar()" aria-label="Open menu">☰</button>

<div class="sidebar" id="sidebar">
    <h2>Menu</h2>
    <ul>
        <li><a href="${pageContext.request.contextPath}/profile">👤 My Profile</a></li>
        <li><a href="${pageContext.request.contextPath}/favorites">⭐ Favorites</a></li>
        <li><a href="${pageContext.request.contextPath}/upload-note">📤 Upload notes</a></li>
    </ul>
    <form action="${pageContext.request.contextPath}/logout" method="post" class="logout-form">
        <button type="submit" class="logout-btn">🚪 Logout</button>
    </form>
</div>

<div class="overlay" id="overlay" onclick="toggleSidebar()"></div>

<div class="container">

    <%-- Navigazione orizzontale: visibile solo su desktop (nascosta su mobile via CSS) --%>
    <nav class="topnav">
        <a href="${pageContext.request.contextPath}/profile">👤 My Profile</a>
        <a href="${pageContext.request.contextPath}/favorites">⭐ Favorites</a>
        <a href="${pageContext.request.contextPath}/upload-note">📤 Upload notes</a>
        <form action="${pageContext.request.contextPath}/logout" method="post">
            <button type="submit">🚪 Logout</button>
        </form>
    </nav>

    <%-- Welcome text: retrocesso da h1 a p per correggere la doppia intestazione --%>
    <p class="welcome-text">Welcome, <c:out value="${sessionScope.user.name}"/>!</p>

    <h1>Search Notes</h1>

    <div class="search-box">
        <input type="text" id="query" placeholder="Search by author, course or content..." required>
        <button type="button" id="searchBtn" aria-label="Search">🔍</button>
    </div>

    <div id="courseStrip" class="course-strip"></div>

    <div id="results"></div>
</div>

<script>const BASE_URL = "${pageContext.request.contextPath}";</script>
<script src="${pageContext.request.contextPath}/js/sidebar.js"></script>
<script src="${pageContext.request.contextPath}/js/notes.js?v=2"></script>

</body>
</html>
