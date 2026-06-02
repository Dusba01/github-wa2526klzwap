<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

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

    <c:if test="${not empty param.success}">
        <div class="feedback success"><c:out value="${param.success}"/></div>
    </c:if>
    <c:if test="${not empty param.error}">
        <div class="feedback error"><c:out value="${param.error}"/></div>
    </c:if>

    <form action="${pageContext.request.contextPath}/upload-note" method="post" enctype="multipart/form-data">
        <div class="form-group">
            <label for="title">Notebook title</label>
            <input type="text" id="title" name="title" placeholder="e.g. Algorithms Week 3 Notes" required>
        </div>

        <div class="form-group">
            <label for="courseId">Course</label>
            <select id="courseId" name="courseId" required>
                <option value="">Select a course</option>
                <c:forEach var="course" items="${courses}">
                    <option value="${course.id}"><c:out value="${course.name}"/></option>
                </c:forEach>
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
