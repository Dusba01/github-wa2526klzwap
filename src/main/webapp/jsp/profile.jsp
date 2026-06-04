<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Profile</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/base.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/layout.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/components.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/profile.css">
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
        <div class="brand">Student Profile</div>
        <a class="nav-link" href="${pageContext.request.contextPath}/jsp/home.jsp">← Back to home</a>
    </div>

    <div class="grid-profile">
        <section class="panel">
            <div class="user-badge"><c:out value="${badgeLetter}"/></div>
            <p class="muted">Your account information and upload activity.</p>

            <div class="info-list">
                <div class="info-item">
                    <div class="info-label">Username</div>
                    <div><c:out value="${not empty userProfile.username ? userProfile.username : '-'}"/></div>
                </div>
                <div class="info-item">
                    <div class="info-label">Full Name</div>
                    <div><c:out value="${not empty userProfile.name ? userProfile.name : '-'}"/></div>
                </div>
                <div class="info-item">
                    <div class="info-label">Email</div>
                    <div><c:out value="${not empty userProfile.email ? userProfile.email : '-'}"/></div>
                </div>
            </div>

            <div class="stats">
                <div class="stat-card">
                    <div class="stat-number">${fn:length(uploadedNotes)}</div>
                    <div class="muted">Uploads</div>
                </div>
            </div>
        </section>

        <section class="panel">
            <div class="section-title">
                <div>
                    <h2>Your Uploads</h2>
                    <p class="muted">All notes uploaded from this account.</p>
                </div>
                <a class="nav-link" href="${pageContext.request.contextPath}/upload-note">+ New upload</a>
            </div>

            <div class="upload-list">
                <c:choose>
                    <c:when test="${not empty uploadedNotes}">
                        <c:forEach var="note" items="${uploadedNotes}">
                            <article class="upload-card">
                                <h3><c:out value="${note.title}"/></h3>
                                <p class="muted">
                                    <c:choose>
                                        <c:when test="${not empty note.description}">
                                            <c:out value="${note.description}"/>
                                        </c:when>
                                        <c:otherwise>No description available.</c:otherwise>
                                    </c:choose>
                                </p>
                                <div class="upload-meta">
                                    <span>📚 <c:out value="${not empty note.courseName ? note.courseName : 'Unknown course'}"/></span>
                                    <span>🕒 <c:out value="${note.uploadDateFormatted}"/></span>
                                </div>
                                <div class="upload-actions">
                                    <a class="download-btn" href="${pageContext.request.contextPath}/download-note?id=${note.id}">⬇ Download</a>
                                    <details class="edit-panel">
                                        <summary class="edit-btn edit-toggle">✏ Edit</summary>
                                        <form class="edit-form" action="${pageContext.request.contextPath}/update-note" method="post">
                                            <input type="hidden" name="noteId" value="${note.id}">
                                            <div class="field-grid">
                                                <label>
                                                    Title
                                                    <input type="text" name="title" value="<c:out value="${note.title}"/>" required>
                                                </label>
                                                <label>
                                                    Course
                                                    <select name="courseId" required>
                                                        <c:forEach var="course" items="${courses}">
                                                            <option value="${course.id}" ${course.id == note.courseId ? 'selected' : ''}>
                                                                <c:out value="${course.name}"/>
                                                            </option>
                                                        </c:forEach>
                                                    </select>
                                                </label>
                                            </div>
                                            <label>
                                                Description
                                                <textarea name="description"><c:out value="${note.description}"/></textarea>
                                            </label>
                                            <div class="edit-actions">
                                                <button type="submit" class="edit-btn">Save changes</button>
                                            </div>
                                        </form>
                                    </details>
                                    <form class="delete-form" action="${pageContext.request.contextPath}/delete-note" method="post"
                                          onsubmit="return confirm('Delete this upload permanently?');">
                                        <input type="hidden" name="noteId" value="${note.id}">
                                        <button type="submit" class="delete-btn">🗑 Delete</button>
                                    </form>
                                </div>
                            </article>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <div class="empty-state">
                            <h3>No uploads yet</h3>
                            <p>Your shared notes will appear here after you upload your first PDF.</p>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </section>
    </div>
</div>
</body>
</html>
