package controller.Note;

import controller.AbstractDatabaseServlet;
import dao.note.DeleteNoteByIdAndAuthorIdDAO;
import dao.note.GetNoteByIdAndAuthorIdDAO;
import dao.rating.DeleteRatingsByNoteIdDAO;
import jakarta.servlet.annotation.WebServlet;
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
public class DeleteNoteServlet extends AbstractDatabaseServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // AuthenticationFilter guarantees an authenticated user in the session.
        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("user");

        Integer noteId = parseNoteId(req.getParameter("noteId"));
        if (noteId == null) {
            resp.sendRedirect(req.getContextPath() + "/profile?error="
                    + URLEncoder.encode("Invalid note selected.", StandardCharsets.UTF_8));
            return;
        }

        try {
            Note note = new GetNoteByIdAndAuthorIdDAO(getConnection(), noteId, user.getId())
                    .access().getOutputParam();
            if (note == null) {
                resp.sendRedirect(req.getContextPath() + "/profile?error="
                        + URLEncoder.encode("Upload not found.", StandardCharsets.UTF_8));
                return;
            }

            if (note.getFilePath() != null && !note.getFilePath().isBlank()) {
                StorageService.getInstance().delete(note.getFilePath());
            }

            new DeleteRatingsByNoteIdDAO(getConnection(), noteId).access();
            new DeleteNoteByIdAndAuthorIdDAO(getConnection(), noteId, user.getId()).access();

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
