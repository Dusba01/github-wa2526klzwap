package controller.Note;

import controller.AbstractDatabaseServlet;
import dao.course.ListCoursesDAO;
import dao.note.CreateNoteDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
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
        fileSizeThreshold = 2 * 1024 * 1024,   // buffer up to 2 MB in memory, then spill to disk
        maxFileSize = 50L * 1024 * 1024,       // max size of a single uploaded file (50 MB)
        maxRequestSize = 55L * 1024 * 1024     // max size of the whole multipart request (55 MB)
)
public class UploadNoteServlet extends AbstractDatabaseServlet {

    /** Human-readable cap, kept in sync with maxFileSize above for error messages. */
    private static final long MAX_FILE_SIZE_MB = 50L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            req.setAttribute("courses", new ListCoursesDAO(getConnection()).access().getOutputListParam());
            req.getRequestDispatcher("/jsp/upload.jsp").forward(req, resp);
        } catch (SQLException e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/jsp/home.jsp?error=Unable to load courses.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {
        // AuthenticationFilter guarantees an authenticated user in the session.
        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("user");

        // Reading parameters/parts forces Tomcat to parse the multipart body.
        // If the upload exceeds the configured limits, that parsing throws an
        // (unchecked) IllegalStateException, so it must be guarded here -
        // otherwise the exception escapes and the user gets a raw HTTP 500.
        String title;
        String courseIdValue;
        String description;
        Part pdfPart;
        try {
            title = trim(req.getParameter("title"));
            courseIdValue = trim(req.getParameter("courseId"));
            description = trim(req.getParameter("description"));
            pdfPart = req.getPart("pdfFile");
        } catch (IllegalStateException e) {
            // Thrown when maxFileSize / maxRequestSize is exceeded.
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/upload-note?error="
                    + URLEncoder.encode("File is too large. Maximum allowed size is "
                            + MAX_FILE_SIZE_MB + " MB.", StandardCharsets.UTF_8));
            return;
        }

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

            new CreateNoteDAO(getConnection(), note).access();

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
        } catch (RuntimeException e) {
            // The AWS S3 SDK reports failures (bad bucket/credentials, endpoint
            // unreachable, signature mismatch, etc.) as unchecked exceptions
            // such as S3Exception / SdkClientException. Without this catch they
            // escape as a bare HTTP 500. Surface the real cause instead.
            e.printStackTrace();
            String detail = e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName();
            resp.sendRedirect(req.getContextPath() + "/upload-note?error="
                    + URLEncoder.encode("Storage error: " + detail, StandardCharsets.UTF_8));
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
            return StorageService.getInstance().upload(inputStream, storedFileName, pdfPart.getSize());
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
