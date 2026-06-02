<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Course" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Upload Notes</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/base.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/layout.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/components.css">
</head>
<body class="upload-body">
<div class="form-card">
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

<script src="${pageContext.request.contextPath}/js/upload.js"></script>
</body>
</html>