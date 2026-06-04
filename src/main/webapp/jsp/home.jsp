<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Home</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/base.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/layout.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/components.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/home.css">
</head>
<body>

<%-- Hamburger button: visible on mobile only (hidden on desktop via CSS) --%>
<button class="menu-btn" id="menuBtn" onclick="toggleSidebar()" aria-label="Open menu">☰</button>

<div class="sidebar" id="sidebar">
    <%-- User badge and name at the top of the sidebar --%>
    <div class="sidebar-header">
        <div class="user-badge-sm"><c:out value="${badgeLetter}"/></div>
        <span class="sidebar-name"><c:out value="${displayName}"/></span>
    </div>
    <ul>
        <li><a href="${pageContext.request.contextPath}/profile">👤 My Profile</a></li>
        <li><a href="${pageContext.request.contextPath}/favorites">⭐ Favorites</a></li>
        <li><a href="${pageContext.request.contextPath}/upload-note">📤 Upload notes</a></li>
    </ul>
    <form action="${pageContext.request.contextPath}/logout" method="post" class="logout-form">
        <button type="submit" class="logout-btn">⇦ Logout</button>
    </form>
</div>

<div class="overlay" id="overlay" onclick="toggleSidebar()"></div>

<div class="container">

    <%-- Desktop horizontal navigation: hidden on mobile via CSS --%>
    <nav class="topnav">
        <div class="topnav-user">
            <div class="topnav-badge"><c:out value="${badgeLetter}"/></div>
            <span class="topnav-name"><c:out value="${displayName}"/></span>
        </div>
        <div class="topnav-links">
            <a href="${pageContext.request.contextPath}/profile">👤 My Profile</a>
            <a href="${pageContext.request.contextPath}/favorites">⭐ Favorites</a>
            <a href="${pageContext.request.contextPath}/upload-note">📤 Upload notes</a>
            <form action="${pageContext.request.contextPath}/logout" method="post">
                <button type="submit">🚪 Logout</button>
            </form>
        </div>
    </nav>

    <c:if test="${not empty param.success}">
        <div class="feedback success"><c:out value="${param.success}"/></div>
    </c:if>
    <c:if test="${not empty param.error}">
        <div class="feedback error"><c:out value="${param.error}"/></div>
    </c:if>

    <p class="welcome-text">Welcome, <c:out value="${displayName}"/>!</p>

    <h1>Search Notes</h1>

    <div class="search-box">
        <input type="text" id="query" placeholder="Search by author, course or content..." required>
        <button type="button" id="resetBtn" class="icon-btn reset-btn" aria-label="Reset search" title="Clear search">✖️</button>
        <span class="search-divider" aria-hidden="true"></span>
        <button type="button" id="searchBtn" class="icon-btn" aria-label="Search" title="Search">🔍</button>
    </div>

    <div id="courseStrip" class="course-strip"></div>

    <div id="results"></div>
</div>

<script>const BASE_URL = "${pageContext.request.contextPath}";</script>
<script src="${pageContext.request.contextPath}/js/sidebar.js"></script>
<script src="${pageContext.request.contextPath}/js/notes.js"></script>

</body>
</html>
