<%@ page contentType="text/html;charset=UTF-8" language="java" import="java.util.List,model.Note" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Favorites</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <style>
        * { box-sizing: border-box; }

        body {
            margin: 0;
            min-height: 100vh;
            font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #dbeafe 0%, #f8fafc 45%, #fef3c7 100%);
            color: #0f172a;
            padding: 32px 20px 48px;
        }

        .page {
            width: 100%;
            max-width: 1080px;
            margin: 0 auto;
        }

        .topbar {
            display: flex;
            justify-content: space-between;
            align-items: center;
            gap: 14px;
            margin-bottom: 28px;
        }

        .title-block h1 {
            margin: 0 0 6px;
            font-size: 2rem;
        }

        .muted { color: #64748b; }

        .nav-link {
            display: inline-flex;
            align-items: center;
            text-decoration: none;
            padding: 11px 16px;
            border-radius: 999px;
            color: #0f172a;
            background: rgba(255,255,255,0.92);
            box-shadow: 0 10px 24px rgba(15, 23, 42, 0.08);
        }

        .grid {
            display: grid;
            gap: 16px;
        }

        .card {
            background: rgba(255,255,255,0.94);
            border: 1px solid rgba(148, 163, 184, 0.18);
            border-radius: 22px;
            padding: 22px;
            box-shadow: 0 18px 45px rgba(15, 23, 42, 0.08);
        }

        .card h3 {
            margin: 0 0 10px;
            font-size: 1.45rem;
        }

        .card p {
            margin: 0 0 14px;
            line-height: 1.55;
            color: #475569;
        }

        .meta {
            display: flex;
            gap: 14px;
            flex-wrap: wrap;
            margin-bottom: 18px;
            color: #475569;
        }

        .actions {
            display: flex;
            gap: 12px;
            flex-wrap: wrap;
            align-items: center;
        }

        .btn {
            border: none;
            display: inline-flex;
            align-items: center;
            justify-content: center;
            gap: 8px;
            min-height: 46px;
            padding: 11px 16px;
            border-radius: 999px;
            text-decoration: none;
            font: inherit;
            font-weight: 600;
            cursor: pointer;
        }

        .btn-download {
            color: white;
            background: linear-gradient(135deg, #0f766e, #14b8a6);
            box-shadow: 0 12px 24px rgba(20, 184, 166, 0.22);
        }

        .btn-remove {
            color: #e11d48;
            background: linear-gradient(135deg, #ffe4e6, #fecdd3);
            box-shadow: 0 12px 24px rgba(225, 29, 72, 0.16);
        }

        .empty-state {
            padding: 34px;
            border-radius: 22px;
            background: rgba(255,255,255,0.88);
            text-align: center;
            color: #475569;
            box-shadow: 0 18px 45px rgba(15, 23, 42, 0.06);
        }

        @media (max-width: 640px) {
            .topbar {
                flex-direction: column;
                align-items: stretch;
            }

            .actions {
                flex-direction: column;
                align-items: stretch;
            }

            .btn {
                width: 100%;
            }
        }
    </style>
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

    <div class="grid" id="favoritesGrid">
        <%
            if (favoriteNotes != null && !favoriteNotes.isEmpty()) {
                for (Note note : favoriteNotes) {
        %>
        <article class="card" id="favorite-card-<%= note.getId() %>">
            <h3><%= note.getTitle() %></h3>
            <p><%= note.getDescription() != null && !note.getDescription().isBlank() ? note.getDescription() : "No description available." %></p>
            <div class="meta">
                <span>📚 <%= note.getCourseName() %></span>
                <span>👤 <%= note.getAuthorUsername() %></span>
                <span>🕒 <%= note.getUploadDate() != null ? note.getUploadDate().toLocalDate() : "Unknown date" %></span>
            </div>
            <div class="actions">
                <a class="btn btn-download" href="${pageContext.request.contextPath}/download-note?id=<%= note.getId() %>">⬇ Download</a>
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
