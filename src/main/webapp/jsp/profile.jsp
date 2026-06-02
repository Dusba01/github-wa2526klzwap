<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Profilo – StudyShare</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/base.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/forms.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/home.css">
    <style>
        body { background: linear-gradient(135deg, #dbeafe 0%, #dcfce7 100%); min-height: 100vh; }

        /* Card profilo */
        .profile-card {
            background: rgba(255,255,255,0.75);
            backdrop-filter: blur(10px);
            border-radius: 20px;
            padding: 32px;
            margin-bottom: 28px;
            border: 1px solid rgba(255,255,255,0.8);
            box-shadow: 0 4px 20px rgba(0,0,0,0.08);
        }

        .profile-avatar {
            width: 72px;
            height: 72px;
            border-radius: 50%;
            background: linear-gradient(135deg, #6366f1, #8b5cf6);
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 2rem;
            margin-bottom: 16px;
        }

        .profile-name {
            font-size: 1.4rem;
            font-weight: 700;
            color: #1e293b;
        }

        .profile-email {
            color: #64748b;
            font-size: 0.93rem;
            margin-top: 4px;
        }

        .stats-row {
            display: flex;
            gap: 16px;
            flex-wrap: wrap;
            margin-top: 20px;
        }

        .stat-box {
            background: rgba(99,102,241,0.08);
            border-radius: 12px;
            padding: 14px 20px;
            text-align: center;
            flex: 1;
            min-width: 100px;
        }

        .stat-box .stat-number {
            font-size: 1.5rem;
            font-weight: 700;
            color: #4f46e5;
        }

        .stat-box .stat-label {
            font-size: 0.82rem;
            color: #64748b;
            margin-top: 2px;
        }

        /* Sezione materiali caricati */
        .section-title {
            font-size: 1.1rem;
            font-weight: 700;
            color: #1e293b;
            margin-bottom: 16px;
        }

        /* Download nel profilo */
        .download-btn-profile {
            display: inline-flex;
            align-items: center;
            gap: 6px;
            padding: 6px 14px;
            background: #f1f5f9;
            color: #374151;
            border-radius: 999px;
            font-size: 0.85rem;
            font-weight: 500;
            text-decoration: none;
            border: 1px solid #e2e8f0;
            transition: background 0.18s;
        }

        .download-btn-profile:hover {
            background: #e2e8f0;
        }
    </style>
</head>
<body>
<%
    String username = (String) session.getAttribute("username");
    String email    = (String) session.getAttribute("email");
    List<?> uploads = (List<?>) request.getAttribute("uploads");
%>

<div class="page">
    <div class="topbar">
        <h1>👤 Profilo</h1>
        <div style="display:flex; gap:10px; flex-wrap:wrap;">
            <a href="${pageContext.request.contextPath}/home" class="nav-link">🏠 Home</a>
            <a href="${pageContext.request.contextPath}/upload" class="nav-link">📤 Carica</a>
            <a href="${pageContext.request.contextPath}/favorites" class="nav-link">⭐ Preferiti</a>
        </div>
    </div>

    <!-- Feedback -->
    <% String error = (String) request.getAttribute("error"); %>
    <% String success = (String) request.getAttribute("success"); %>
    <% if (error != null) { %>
    <div class="feedback error"><%= error %></div>
    <% } %>
    <% if (success != null) { %>
    <div class="feedback success"><%= success %></div>
    <% } %>

    <!-- Profilo card -->
    <div class="profile-card">
        <div class="profile-avatar">👤</div>
        <div class="profile-name"><%= username != null ? username : "Utente" %></div>
        <div class="profile-email"><%= email != null ? email : "" %></div>
        <div class="stats-row">
            <div class="stat-box">
                <div class="stat-number"><%= uploads != null ? uploads.size() : 0 %></div>
                <div class="stat-label">Caricamenti</div>
            </div>
        </div>
    </div>

    <!-- Materiali caricati -->
    <div class="section-title">📁 I Miei Materiali</div>
    <%
        if (uploads == null || uploads.isEmpty()) {
    %>
    <div class="empty-state">
        <p>Non hai ancora caricato materiali.</p>
        <a href="${pageContext.request.contextPath}/upload" class="nav-link">📤 Carica ora</a>
    </div>
    <%
    } else {
    %>
    <div class="results-grid">
        <%
            for (Object item : uploads) {
        %>
        <div class="card">
            <p><%= item.toString() %></p>
            <div class="card-actions">
                <a href="#" class="download-btn-profile">⬇ Scarica</a>
            </div>
        </div>
        <%
            }
        %>
    </div>
    <%
        }
    %>

    <!-- Modifica password -->
    <div class="profile-card" style="margin-top:28px;">
        <div class="section-title">🔒 Cambia Password</div>
        <form action="${pageContext.request.contextPath}/profile" method="post">
            <div class="form-group">
                <label for="currentPassword">Password attuale</label>
                <input type="password" id="currentPassword" name="currentPassword" required>
            </div>
            <div class="form-group">
                <label for="newPassword">Nuova password</label>
                <input type="password" id="newPassword" name="newPassword" required>
                <div class="helper-text">Minimo 8 caratteri</div>
            </div>
            <div class="form-group">
                <label for="confirmNewPassword">Conferma nuova password</label>
                <input type="password" id="confirmNewPassword" name="confirmNewPassword" required>
            </div>
            <div class="form-actions">
                <button type="submit" class="btn btn-primary" style="width:auto;">Aggiorna Password</button>
            </div>
        </form>
    </div>

</div>
</body>
</html>