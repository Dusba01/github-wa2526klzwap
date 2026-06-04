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
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/base.css?v=4">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/layout.css?v=4">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/components.css?v=4">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/home.css?v=4">
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
        <button type="button" id="resetBtn" class="icon-btn reset-btn" aria-label="Reset search" title="Clear search">
            <svg viewBox="0 0 24 24" aria-hidden="true">
                <path d="M19 6.41 17.59 5 12 10.59 6.41 5 5 6.41 10.59 12 5 17.59 6.41 19 12 13.41 17.59 19 19 17.59 13.41 12z"/>
            </svg>
        </button>
        <span class="search-divider" aria-hidden="true"></span>
        <button type="button" id="searchBtn" class="icon-btn" aria-label="Search" title="Search">
            <svg viewBox="0 0 24 24" aria-hidden="true">
                <path d="M15.5 14h-.79l-.28-.27C15.41 12.59 16 11.11 16 9.5 16 5.91 13.09 3 9.5 3S3 5.91 3 9.5 5.91 16 9.5 16c1.61 0 3.09-.59 4.23-1.57l.27.28v.79l5 4.99L20.49 19l-4.99-5zm-6 0C7.01 14 5 11.99 5 9.5S7.01 5 9.5 5 14 7.01 14 9.5 11.99 14 9.5 14z"/>
            </svg>
        </button>
    </div>

    <div id="courseStrip" class="course-strip"></div>

    <div id="results"></div>
</div>

<script>const BASE_URL = "${pageContext.request.contextPath}";</script>
<script src="${pageContext.request.contextPath}/js/sidebar.js"></script>
<script src="${pageContext.request.contextPath}/js/notes.js?v=3"></script>

</body>
</html>
