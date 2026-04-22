package controller.Note;

import dao.NoteDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Note;
import model.User;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

@WebServlet("/update-note")
public class UpdateNoteServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        User user = session != null ? (User) session.getAttribute("user") : null;

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/jsp/login.jsp?error=login_required");
            return;
        }

        Integer noteId = parsePositiveInt(req.getParameter("noteId"));
        Integer courseId = parsePositiveInt(req.getParameter("courseId"));
        String title = trim(req.getParameter("title"));
        String description = trim(req.getParameter("description"));

        if (noteId == null || courseId == null || title == null) {
            resp.sendRedirect(req.getContextPath() + "/profile?error="
                    + URLEncoder.encode("Please fill in title and course before saving.", StandardCharsets.UTF_8));
            return;
        }

        try {
            Note existingNote = NoteDAO.getNoteByIdAndAuthorId(noteId, user.getId());
            if (existingNote == null) {
                resp.sendRedirect(req.getContextPath() + "/profile?error="
                        + URLEncoder.encode("Upload not found.", StandardCharsets.UTF_8));
                return;
            }

            existingNote.setCourseId(courseId);
            existingNote.setTitle(title);
            existingNote.setDescription(description);

            boolean updated = NoteDAO.updateNoteByIdAndAuthorId(existingNote);
            if (!updated) {
                resp.sendRedirect(req.getContextPath() + "/profile?error="
                        + URLEncoder.encode("Unable to update the upload.", StandardCharsets.UTF_8));
                return;
            }

            resp.sendRedirect(req.getContextPath() + "/profile?success="
                    + URLEncoder.encode("Upload updated successfully.", StandardCharsets.UTF_8));
        } catch (SQLException e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/profile?error="
                    + URLEncoder.encode("Database error while updating the upload.", StandardCharsets.UTF_8));
        }
    }

    private Integer parsePositiveInt(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        try {
            int parsed = Integer.parseInt(value);
            return parsed > 0 ? parsed : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private String trim(String value) {
        if (value == null) {
            return null;
        }

        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
