<%@ page contentType="text/html;charset=UTF-8" language="java" import="java.util.List,model.Note,model.User,model.Course" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Profile</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <style>
        * {
            box-sizing: border-box;
        }

        body {
            margin: 0;
            min-height: 100vh;
            font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #dbeafe 0%, #f8fafc 42%, #dcfce7 100%);
            color: #0f172a;
            padding: 32px 20px 48px;
        }

        .page {
            width: 100%;
            max-width: 1000px;
            margin: 0 auto;
        }

        .topbar {
            display: flex;
            justify-content: space-between;
            align-items: center;
            gap: 16px;
            margin-bottom: 28px;
        }

        .brand {
            font-size: 1.8rem;
            font-weight: 700;
        }

        .nav-link {
            display: inline-flex;
            align-items: center;
            text-decoration: none;
            padding: 11px 16px;
            border-radius: 999px;
            color: #0f172a;
            background: rgba(255, 255, 255, 0.88);
            box-shadow: 0 8px 22px rgba(15, 23, 42, 0.08);
        }

        .grid {
            display: grid;
            grid-template-columns: 320px 1fr;
            gap: 24px;
        }

        .panel {
            background: rgba(255, 255, 255, 0.94);
            border: 1px solid rgba(148, 163, 184, 0.18);
            border-radius: 24px;
            padding: 24px;
            box-shadow: 0 18px 45px rgba(15, 23, 42, 0.08);
        }

        .user-badge {
            width: 72px;
            height: 72px;
            border-radius: 20px;
            display: flex;
            align-items: center;
            justify-content: center;
            background: linear-gradient(135deg, #0f766e, #38bdf8);
            color: white;
            font-size: 1.8rem;
            font-weight: 700;
            margin-bottom: 18px;
        }

        h1, h2, h3, p {
            margin-top: 0;
        }

        .user-name {
            margin-bottom: 14px;
            font-size: 1rem;
        }

        .muted {
            color: #64748b;
        }

        .info-list {
            display: grid;
            gap: 16px;
            margin-top: 22px;
        }

        .info-item {
            padding: 14px 16px;
            border-radius: 16px;
            background: #f8fafc;
        }

        .info-label {
            font-size: 0.8rem;
            text-transform: uppercase;
            letter-spacing: 0.08em;
            color: #64748b;
            margin-bottom: 6px;
        }

        .stats {
            display: flex;
            gap: 14px;
            flex-wrap: wrap;
            margin-top: 22px;
        }

        .stat-card {
            flex: 1;
            min-width: 120px;
            padding: 16px;
            border-radius: 18px;
            background: linear-gradient(135deg, #ecfeff, #eff6ff);
        }

        .stat-number {
            font-size: 1.6rem;
            font-weight: 700;
            margin-bottom: 6px;
        }

        .section-title {
            display: flex;
            justify-content: space-between;
            align-items: center;
            gap: 12px;
            margin-bottom: 18px;
        }

        .upload-list {
            display: grid;
            gap: 16px;
        }

        .upload-card {
            border: 1px solid rgba(15, 118, 110, 0.12);
            border-radius: 18px;
            padding: 18px;
            background: linear-gradient(180deg, rgba(255,255,255,0.98), rgba(248,250,252,0.92));
            overflow: hidden;
        }

        .upload-card h3 {
            margin-bottom: 8px;
        }

        .upload-meta {
            display: flex;
            gap: 14px;
            flex-wrap: wrap;
            margin: 12px 0 16px;
            color: #475569;
            font-size: 0.95rem;
        }

        .feedback {
            padding: 12px 14px;
            margin-bottom: 18px;
            border-radius: 12px;
            font-size: 0.95rem;
        }

        .feedback.success {
            background: #dcfce7;
            color: #166534;
        }

        .feedback.error {
            background: #fee2e2;
            color: #991b1b;
        }

        .download-btn {
            display: inline-flex;
            align-items: center;
            justify-content: center;
            gap: 8px;
            padding: 11px 16px;
            border-radius: 999px;
            background: linear-gradient(135deg, #0f766e, #14b8a6);
            color: #fff;
            text-decoration: none;
            font-weight: 600;
            box-shadow: 0 12px 24px rgba(20, 184, 166, 0.2);
            min-height: 44px;
            min-width: 168px;
        }

        .edit-btn {
            border: none;
            display: inline-flex;
            align-items: center;
            justify-content: center;
            gap: 8px;
            padding: 11px 16px;
            border-radius: 999px;
            background: linear-gradient(135deg, #2563eb, #38bdf8);
            color: #fff;
            font: inherit;
            font-weight: 600;
            cursor: pointer;
            box-shadow: 0 12px 24px rgba(37, 99, 235, 0.18);
            min-height: 44px;
            min-width: 124px;
        }

        .upload-actions {
            display: flex;
            gap: 10px;
            flex-wrap: wrap;
            align-items: center;
        }

        .delete-form {
            margin: 0;
        }

        .delete-btn {
            border: none;
            display: inline-flex;
            align-items: center;
            gap: 8px;
            padding: 11px 16px;
            border-radius: 999px;
            background: linear-gradient(135deg, #dc2626, #f97316);
            color: #fff;
            font: inherit;
            font-weight: 600;
            cursor: pointer;
            box-shadow: 0 12px 24px rgba(220, 38, 38, 0.18);
            min-height: 44px;
            min-width: 132px;
        }

        .edit-panel {
            margin: 0;
            padding: 0;
            border: none;
            display: flex;
            align-items: center;
        }

        .edit-panel summary {
            list-style: none;
            display: inline-flex;
            align-items: center;
        }

        .edit-panel summary::-webkit-details-marker {
            display: none;
        }

        .edit-toggle {
            user-select: none;
        }

        .edit-panel[open] .edit-toggle {
            background: linear-gradient(135deg, #1d4ed8, #0ea5e9);
        }

        .edit-panel[open] {
            display: block;
            width: 100%;
            margin-top: 16px;
            padding-top: 16px;
            border-top: 1px solid rgba(148, 163, 184, 0.18);
        }

        .edit-form {
            display: grid;
            gap: 14px;
            margin-top: 14px;
            padding: 18px;
            border-radius: 18px;
            background: linear-gradient(135deg, #eff6ff, #f8fafc 55%, #ecfeff);
            border: 1px solid rgba(59, 130, 246, 0.12);
        }

        .edit-form label {
            display: grid;
            gap: 6px;
            font-weight: 600;
            color: #334155;
        }

        .edit-form input,
        .edit-form textarea,
        .edit-form select {
            width: 100%;
            padding: 12px 14px;
            border: 1px solid #cbd5e1;
            border-radius: 12px;
            font: inherit;
            background: #fff;
        }

        .edit-form textarea {
            min-height: 96px;
            resize: vertical;
        }

        .edit-actions {
            display: flex;
            justify-content: flex-end;
            gap: 10px;
            flex-wrap: wrap;
        }

        .field-grid {
            display: grid;
            grid-template-columns: 1.3fr 0.9fr;
            gap: 12px;
        }

        .empty-state {
            padding: 28px;
            border-radius: 18px;
            background: #f8fafc;
            color: #475569;
            text-align: center;
        }

        @media (max-width: 860px) {
            .grid {
                grid-template-columns: 1fr;
            }
        }

        @media (max-width: 560px) {
            .topbar {
                flex-direction: column;
                align-items: stretch;
            }
        }

        @media (max-width: 680px) {
            .field-grid {
                grid-template-columns: 1fr;
            }

            .upload-actions {
                flex-direction: column;
                align-items: stretch;
            }

            .download-btn,
            .edit-panel,
            .edit-btn,
            .delete-btn {
                width: 100%;
                min-width: 0;
            }

            .edit-panel summary {
                width: 100%;
            }
        }
    </style>
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

    <div class="grid">
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
