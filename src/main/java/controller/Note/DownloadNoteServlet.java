package controller.Note;

import dao.NoteDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Note;
import utils.StorageService;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

@WebServlet("/download-note")
public class DownloadNoteServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String noteIdValue = req.getParameter("id");
        Integer noteId = parseNoteId(noteIdValue);

        if (noteId == null) {
            resp.sendRedirect(req.getContextPath() + "/jsp/home.jsp?error="
                    + URLEncoder.encode("Invalid note id.", StandardCharsets.UTF_8));
            return;
        }

        try {
            Note note = NoteDAO.getNoteById(noteId);
            if (note == null || note.getFilePath() == null || note.getFilePath().isBlank()) {
                resp.sendRedirect(req.getContextPath() + "/jsp/home.jsp?error="
                        + URLEncoder.encode("File not found.", StandardCharsets.UTF_8));
                return;
            }

            StorageService.StoredFile storedFile = new StorageService().download(note.getFilePath());

            resp.setContentType(storedFile.getContentType());
            resp.setHeader("Content-Disposition", "attachment; filename=\"" + storedFile.getFileName() + "\"");
            resp.setContentLength(storedFile.getBytes().length);
            resp.getOutputStream().write(storedFile.getBytes());
        } catch (SQLException e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error.");
        } catch (RuntimeException e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Download error.");
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
