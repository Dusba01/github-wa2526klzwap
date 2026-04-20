<%@ page contentType="text/html;charset=UTF-8" language="java" import="java.util.List,model.Course" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Upload Notes</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <style>
        * {
            box-sizing: border-box;
        }

        body {
            margin: 0;
            min-height: 100vh;
            font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #e0f2fe 0%, #f8fafc 45%, #dcfce7 100%);
            color: #1f2937;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 24px;
        }

        .upload-card {
            width: 100%;
            max-width: 640px;
            background: rgba(255, 255, 255, 0.94);
            border: 1px solid rgba(148, 163, 184, 0.22);
            border-radius: 20px;
            padding: 32px;
            box-shadow: 0 20px 50px rgba(15, 23, 42, 0.12);
        }

        h1 {
            margin: 0 0 10px;
            font-size: 2rem;
        }

        .subtitle {
            margin: 0 0 28px;
            color: #475569;
            line-height: 1.5;
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

        .form-group {
            margin-bottom: 18px;
        }

        label {
            display: block;
            margin-bottom: 8px;
            font-weight: 600;
        }

        input,
        textarea,
        select {
            width: 100%;
            padding: 13px 14px;
            border: 1px solid #cbd5e1;
            border-radius: 12px;
            font: inherit;
            background: #fff;
        }

        textarea {
            min-height: 110px;
            resize: vertical;
        }

        input[type="file"] {
            padding: 10px;
            background: #f8fafc;
        }

        .helper-text {
            margin-top: 7px;
            font-size: 0.9rem;
            color: #64748b;
        }

        .actions {
            display: flex;
            gap: 12px;
            flex-wrap: wrap;
            margin-top: 26px;
        }

        .btn {
            border: none;
            border-radius: 12px;
            padding: 13px 18px;
            font: inherit;
            font-weight: 600;
            cursor: pointer;
            text-decoration: none;
            transition: transform 0.15s ease, box-shadow 0.15s ease;
        }

        .btn-primary {
            background: #0f766e;
            color: #fff;
            box-shadow: 0 12px 24px rgba(15, 118, 110, 0.22);
        }

        .btn-secondary {
            background: #e2e8f0;
            color: #1e293b;
        }

        .btn:hover {
            transform: translateY(-1px);
        }

        @media (max-width: 640px) {
            .upload-card {
                padding: 24px;
            }

            h1 {
                font-size: 1.7rem;
            }
        }
    </style>
</head>
<body>
<div class="upload-card">
    <h1>Upload a PDF</h1>
    <p class="subtitle">Share class notes with other students by uploading a PDF and adding a few details.</p>

    <%
        String successMessage = request.getParameter("success");
        String errorMessage = request.getParameter("error");
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

    <%
        List<Course> courses = (List<Course>) request.getAttribute("courses");
    %>

    <form action="${pageContext.request.contextPath}/upload-note" method="post" enctype="multipart/form-data">
        <div class="form-group">
            <label for="title">Notebook title</label>
            <input type="text" id="title" name="title" placeholder="e.g. Algorithms Week 3 Notes" required>
        </div>

        <div class="form-group">
            <label for="courseId">Course</label>
            <select id="courseId" name="courseId" required>
                <option value="">Select a course</option>
                <%
                    if (courses != null) {
                        for (Course course : courses) {
                %>
                <option value="<%= course.getId() %>"><%= course.getName() %></option>
                <%
                        }
                    }
                %>
            </select>
            <div class="helper-text">Choose one of the courses already stored in the database.</div>
        </div>

        <div class="form-group">
            <label for="description">Description</label>
            <textarea id="description" name="description" placeholder="Add a short description of the material"></textarea>
        </div>

        <div class="form-group">
            <label for="pdfFile">PDF file</label>
            <input type="file" id="pdfFile" name="pdfFile" accept="application/pdf,.pdf" required>
            <div class="helper-text">Only `.pdf` files are allowed.</div>
        </div>

        <div class="actions">
            <button type="submit" class="btn btn-primary">Upload PDF</button>
            <a href="${pageContext.request.contextPath}/jsp/home.jsp" class="btn btn-secondary">Back to home</a>
        </div>
    </form>
</div>
<script>
    const uploadForm = document.querySelector('form[action$="/upload-note"]');
    const pdfInput = document.getElementById("pdfFile");

    uploadForm.addEventListener("submit", function (event) {
        const file = pdfInput.files[0];

        if (!file) {
            return;
        }

        const fileName = file.name.toLowerCase();
        const fileType = file.type;
        const isPdf = fileName.endsWith(".pdf")
            && (!fileType || fileType === "application/pdf" || fileType === "application/octet-stream");

        if (!isPdf) {
            event.preventDefault();
            alert("Only PDF files can be uploaded.");
            pdfInput.value = "";
        }
    });
</script>
</body>
</html>
