package controller.Note;

import dao.CourseDAO;
import dao.NoteDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import model.Note;
import model.User;
import utils.StorageService;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.UUID;

@WebServlet("/upload-note")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 10 * 1024 * 1024,
        maxRequestSize = 12 * 1024 * 1024
)
public class UploadNoteServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            req.setAttribute("courses", CourseDAO.getAllCourses());
            req.getRequestDispatcher("/jsp/upload.jsp").forward(req, resp);
        } catch (SQLException e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/jsp/home.jsp?error=Unable to load courses.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {
        HttpSession session = req.getSession(false);
        User user = session != null ? (User) session.getAttribute("user") : null;

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/jsp/login.jsp?error=login_required");
            return;
        }

        String title = trim(req.getParameter("title"));
        String courseIdValue = trim(req.getParameter("courseId"));
        String description = trim(req.getParameter("description"));
        Part pdfPart = req.getPart("pdfFile");
        Integer courseId = parseCourseId(courseIdValue);

        if (title == null || courseId == null || pdfPart == null || pdfPart.getSize() == 0) {
            resp.sendRedirect(req.getContextPath() + "/upload-note?error=Please fill in all required fields, select a course, and choose a PDF file.");
            return;
        }

        String submittedFileName = Paths.get(pdfPart.getSubmittedFileName()).getFileName().toString();
        if (!isPdf(pdfPart, submittedFileName)) {
            resp.sendRedirect(req.getContextPath() + "/upload-note?error=Only PDF files are allowed.");
            return;
        }

        try {
            String storedFilePath = uploadToR2(pdfPart, submittedFileName);

            Note note = new Note();
            note.setAuthorId(user.getId());
            note.setCourseId(courseId);
            note.setTitle(title);
            note.setDescription(description);
            note.setFilePath(storedFilePath);

            NoteDAO.saveNote(note);

            resp.sendRedirect(req.getContextPath() + "/upload-note?success=PDF uploaded successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/upload-note?error=Database error while saving the note.");
        } catch (IllegalStateException e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/upload-note?error="
                    + URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/upload-note?error=Unable to upload the PDF to cloud storage.");
        }
    }

    private Integer parseCourseId(String courseIdValue) {
        if (courseIdValue == null) {
            return null;
        }

        try {
            int courseId = Integer.parseInt(courseIdValue);
            return courseId > 0 ? courseId : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private String uploadToR2(Part pdfPart, String submittedFileName) throws IOException {
        String storedFileName = UUID.randomUUID() + "-" + submittedFileName.replaceAll("\\s+", "_");

        try (InputStream inputStream = pdfPart.getInputStream()) {
            return new StorageService().upload(inputStream, storedFileName, pdfPart.getSize());
        }
    }

    private boolean isPdf(Part pdfPart, String submittedFileName) {
        String contentType = pdfPart.getContentType();
        return submittedFileName != null
                && submittedFileName.toLowerCase().endsWith(".pdf")
                && (contentType == null
                || contentType.isBlank()
                || "application/pdf".equalsIgnoreCase(contentType)
                || "application/octet-stream".equalsIgnoreCase(contentType));
    }

    private String trim(String value) {
        if (value == null) {
            return null;
        }

        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
