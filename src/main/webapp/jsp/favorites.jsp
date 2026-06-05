<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Favorites</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/base.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/layout.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/components.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>
<body class="app-body">

<div class="page">

    <c:if test="${not empty param.success}">
        <div class="feedback success"><c:out value="${param.success}"/></div>
    </c:if>
    <c:if test="${not empty param.error}">
        <div class="feedback error"><c:out value="${param.error}"/></div>
    </c:if>

    <div class="topbar">
        <div class="title-block">
            <h1>Your Favorites</h1>
            <div class="muted">Saved notes you want to keep close.</div>
        </div>
        <a class="nav-link" href="${pageContext.request.contextPath}/jsp/home.jsp">← Back to home</a>
    </div>

    <div class="grid-list" id="favoritesGrid">
        <c:choose>
            <c:when test="${not empty favoriteNotes}">
                <c:forEach var="note" items="${favoriteNotes}">
                    <article class="card" id="favorite-card-${note.id}">
                        <h3><c:out value="${note.title}"/></h3>
                        <p>
                            <c:choose>
                                <c:when test="${not empty note.description}">
                                    <c:out value="${note.description}"/>
                                </c:when>
                                <c:otherwise>No description available.</c:otherwise>
                            </c:choose>
                        </p>
                        <div class="card-meta">
                            <span><i class="fa-solid fa-book"></i> <c:out value="${note.courseName}"/></span>
                            <span><i class="fa-solid fa-user"></i> <c:out value="${note.authorUsername}"/></span>
                            <span><i class="fa-solid fa-clock"></i> <c:out value="${note.uploadDateFormatted}"/></span>
                        </div>
                        <div class="card-actions">
                            <a class="download-btn" href="${pageContext.request.contextPath}/download-note?id=${note.id}">
                                <i class="fa-solid fa-download"></i> Download
                            </a>
                            <form action="${pageContext.request.contextPath}/remove-favorite" method="post">
                                <input type="hidden" name="noteId" value="${note.id}">
                                <button type="submit" class="btn btn-remove">
                                    <i class="fa-solid fa-heart-crack"></i> Remove favorite
                                </button>
                            </form>
                        </div>
                    </article>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <div class="empty-state" id="favoritesEmpty">
                    <h3>No favorites yet</h3>
                    <p>Save notes with the heart button from the home page and they will appear here.</p>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>

</body>
</html>
