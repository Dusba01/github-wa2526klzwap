<%@ page contentType="text/html;charset=UTF-8" import="java.util.List,model.Note" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Favorites</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/base.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/layout.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/components.css">
</head>
<body>
<%
    List<Note> favoriteNotes = (List<Note>) request.getAttribute("favoriteNotes");
%>
<div class="page">
    <div class="topbar">
        <div class="title-block">
            <h1>Your Favorites</h1>
            <div class="muted">Saved notes you want to keep close.</div>
        </div>
        <a class="nav-link" href="${pageContext.request.contextPath}/jsp/home.jsp">← Back to home</a>
    </div>

    <div class="grid-list" id="favoritesGrid">
        <%
            if (favoriteNotes != null && !favoriteNotes.isEmpty()) {
                for (Note note : favoriteNotes) {
        %>
        <article class="card" id="favorite-card-<%= note.getId() %>">
            <h3><%= note.getTitle() %></h3>
            <p><%= note.getDescription() != null && !note.getDescription().isBlank() ? note.getDescription() : "No description available." %></p>
            <div class="card-meta">
                <span>📚 <%= note.getCourseName() %></span>
                <span>👤 <%= note.getAuthorUsername() %></span>
                <span>🕒 <%= note.getUploadDate() != null ? note.getUploadDate().toLocalDate() : "Unknown date" %></span>
            </div>
            <div class="card-actions">
                <a class="download-btn" href="${pageContext.request.contextPath}/download-note?id=<%= note.getId() %>">⬇ Download</a>
                <form action="${pageContext.request.contextPath}/remove-favorite" method="post" style="margin:0;">
                    <input type="hidden" name="noteId" value="<%= note.getId() %>">
                    <button type="submit" class="btn btn-remove">♥ Remove favorite</button>
                </form>
            </div>
        </article>
        <%
                }
            } else {
        %>
        <div class="empty-state" id="favoritesEmpty">
            <h3>No favorites yet</h3>
            <p>Save notes with the star button from the home page and they will appear here.</p>
        </div>
        <%
            }
        %>
    </div>
</div>

</body>
</html>
