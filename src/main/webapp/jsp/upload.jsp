<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Carica Materiale – StudyShare</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/base.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/forms.css">
    <style>
        body { background: linear-gradient(135deg, #e0f2fe 0%, #dcfce7 100%); }
    </style>
</head>
<body class="form-page-body">
<div class="form-card wide">
    <h1>📤 Carica Materiale</h1>
    <p class="subtitle">Condividi i tuoi appunti con la community</p>

    <% String error = (String) request.getAttribute("error"); %>
    <% String success = (String) request.getAttribute("success"); %>
    <% if (error != null) { %>
    <div class="feedback error"><%= error %></div>
    <% } %>
    <% if (success != null) { %>
    <div class="feedback success"><%= success %></div>
    <% } %>

    <form action="${pageContext.request.contextPath}/upload" method="post" enctype="multipart/form-data">
        <div class="form-group">
            <label for="title">Titolo *</label>
            <input type="text" id="title" name="title" placeholder="Es. Appunti Analisi I – Capitolo 3" required>
        </div>
        <div class="form-group">
            <label for="description">Descrizione</label>
            <textarea id="description" name="description" placeholder="Breve descrizione del materiale..."></textarea>
        </div>
        <div class="form-group">
            <label for="course">Corso *</label>
            <input type="text" id="course" name="course" placeholder="Es. Analisi Matematica I" required>
        </div>
        <div class="form-group">
            <label for="subject">Materia</label>
            <input type="text" id="subject" name="subject" placeholder="Es. Matematica">
        </div>
        <div class="form-group">
            <label for="file">File *</label>
            <input type="file" id="file" name="file" accept=".pdf,.doc,.docx,.ppt,.pptx,.txt" required>
            <div class="helper-text">Formati supportati: PDF, Word, PowerPoint, TXT (max 10MB)</div>
        </div>
        <div class="form-actions">
            <a href="${pageContext.request.contextPath}/home" class="btn btn-secondary">Annulla</a>
            <button type="submit" class="btn btn-primary" style="width:auto;">Carica</button>
        </div>
    </form>
</div>
</body>
</html>