package controller.Note;

import dao.NoteDAO;
import dao.RatingDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Note;
import model.User;
import utils.StorageService;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

@WebServlet("/delete-note")
public class DeleteNoteServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        User user = session != null ? (User) session.getAttribute("user") : null;

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/jsp/login.jsp?error=login_required");
            return;
        }

        Integer noteId = parseNoteId(req.getParameter("noteId"));
        if (noteId == null) {
            resp.sendRedirect(req.getContextPath() + "/profile?error="
                    + URLEncoder.encode("Invalid note selected.", StandardCharsets.UTF_8));
            return;
        }

        try {
            Note note = NoteDAO.getNoteByIdAndAuthorId(noteId, user.getId());
            if (note == null) {
                resp.sendRedirect(req.getContextPath() + "/profile?error="
                        + URLEncoder.encode("Upload not found.", StandardCharsets.UTF_8));
                return;
            }

            if (note.getFilePath() != null && !note.getFilePath().isBlank()) {
                new StorageService().delete(note.getFilePath());
            }

            RatingDAO.deleteRatingsByNoteId(noteId);
            NoteDAO.deleteNoteByIdAndAuthorId(noteId, user.getId());

            resp.sendRedirect(req.getContextPath() + "/profile?success="
                    + URLEncoder.encode("Upload deleted successfully.", StandardCharsets.UTF_8));
        } catch (SQLException e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/profile?error="
                    + URLEncoder.encode("Database error while deleting the upload.", StandardCharsets.UTF_8));
        } catch (RuntimeException e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/profile?error="
                    + URLEncoder.encode("Cloud storage delete failed.", StandardCharsets.UTF_8));
        }
    }

    private Integer parseNoteId(String noteIdValue) {
        if (noteIdValue == null || noteIdValue.isBlank()) {
            return null;
        }

        try {
            int noteId = Integer.parseInt(noteIdValue);
            return noteId > 0 ? noteId : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
