<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Note" %>
<%@ page import="model.User" %>
<%@ page import="model.Course" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Profile</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/base.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/layout.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/components.css">
</head>
<body>
<%
    User userProfile = (User) request.getAttribute("userProfile");
    List<Note> uploadedNotes = (List<Note>) request.getAttribute("uploadedNotes");
    List<Course> courses = (List<Course>) request.getAttribute("courses");
    String successMessage = request.getParameter("success");
    String errorMessage = request.getParameter("error");
    int uploadCount = uploadedNotes != null ? uploadedNotes.size() : 0;
    String badgeLetter = "?";
    if (userProfile != null) {
        if (userProfile.getName() != null && !userProfile.getName().isBlank()) {
            badgeLetter = userProfile.getName().substring(0, 1).toUpperCase();
        } else if (userProfile.getUsername() != null && !userProfile.getUsername().isBlank()) {
            badgeLetter = userProfile.getUsername().substring(0, 1).toUpperCase();
        }
    }
%>
<div class="page">
    <%
        if (successMessage != null) {
    %>
    <div class="feedback success"><%= successMessage %></div>
    <%
        }
        if (errorMessage != null) {
    %>
    <div class="feedback error"><%= errorMessage %></div>
    <%
        }
    %>
    <div class="topbar">
        <div class="brand">Student Profile</div>
        <a class="nav-link" href="${pageContext.request.contextPath}/jsp/home.jsp">← Back to home</a>
    </div>

    <div class="grid-profile">
        <section class="panel">
            <div class="user-badge"><%= badgeLetter %></div>
            <p class="muted">Your account information and upload activity.</p>

            <div class="info-list">
                <div class="info-item">
                    <div class="info-label">Username</div>
                    <div><%= userProfile != null ? userProfile.getUsername() : "-" %></div>
                </div>
                <div class="info-item">
                    <div class="info-label">Full Name</div>
                    <div><%= userProfile != null ? userProfile.getName() : 0 %></div>
                </div>
                <div class="info-item">
                    <div class="info-label">Email</div>
                    <div><%= userProfile != null ? userProfile.getEmail() : "-" %></div>
                </div>
            </div>

            <div class="stats">
                <div class="stat-card">
                    <div class="stat-number"><%= uploadCount %></div>
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
                <%
                    if (uploadedNotes != null && !uploadedNotes.isEmpty()) {
                        for (Note note : uploadedNotes) {
                %>
                <article class="upload-card">
                    <h3><%= note.getTitle() %></h3>
                    <p class="muted"><%= note.getDescription() != null && !note.getDescription().isBlank() ? note.getDescription() : "No description available." %></p>
                    <div class="upload-meta">
                        <span>📚 <%= note.getCourseName() != null ? note.getCourseName() : "Unknown course" %></span>
                        <span>🕒 <%= note.getUploadDate() != null ? note.getUploadDate().toLocalDate() : "Unknown date" %></span>
                    </div>
                    <div class="upload-actions">
                        <a class="download-btn" href="${pageContext.request.contextPath}/download-note?id=<%= note.getId() %>">⬇ Download</a>
                        <details class="edit-panel">
                            <summary class="edit-btn edit-toggle">✏ Edit</summary>
                            <form class="edit-form" action="${pageContext.request.contextPath}/update-note" method="post">
                                <input type="hidden" name="noteId" value="<%= note.getId() %>">
                                <div class="field-grid">
                                    <label>
                                        Title
                                        <input type="text" name="title" value="<%= note.getTitle() %>" required>
                                    </label>
                                    <label>
                                        Course
                                        <select name="courseId" required>
                                            <%
                                                if (courses != null) {
                                                    for (Course course : courses) {
                                                        boolean selected = course.getId() == note.getCourseId();
                                            %>
                                            <option value="<%= course.getId() %>" <%= selected ? "selected" : "" %>><%= course.getName() %></option>
                                            <%
                                                    }
                                                }
                                            %>
                                        </select>
                                    </label>
                                </div>
                                <label>
                                    Description
                                    <textarea name="description"><%= note.getDescription() != null ? note.getDescription() : "" %></textarea>
                                </label>
                                <div class="edit-actions">
                                    <button type="submit" class="edit-btn">Save changes</button>
                                </div>
                            </form>
                        </details>
                        <form class="delete-form" action="${pageContext.request.contextPath}/delete-note" method="post"
                              onsubmit="return confirm('Delete this upload permanently?');">
                            <input type="hidden" name="noteId" value="<%= note.getId() %>">
                            <button type="submit" class="delete-btn">🗑 Delete</button>
                        </form>
                    </div>
                </article>
                <%
                        }
                    } else {
                %>
                <div class="empty-state">
                    <h3>No uploads yet</h3>
                    <p>Your shared notes will appear here after you upload your first PDF.</p>
                </div>
                <%
                    }
                %>
            </div>
        </section>
    </div>
</div>
</body>
</html>
