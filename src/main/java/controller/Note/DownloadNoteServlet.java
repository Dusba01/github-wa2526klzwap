package controller.Note;

import controller.AbstractDatabaseServlet;
import dao.note.GetNoteByIdDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Note;
import utils.StorageService;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

@WebServlet("/download-note")
public class DownloadNoteServlet extends AbstractDatabaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String noteIdValue = req.getParameter("id");
        Integer noteId = parseNoteId(noteIdValue);

        if (noteId == null) {
            redirectWithError(req, resp, "Invalid note id.");
            return;
        }

        try {
            Note note = new GetNoteByIdDAO(getConnection(), noteId).access().getOutputParam();
            if (note == null || note.getFilePath() == null || note.getFilePath().isBlank()) {
                redirectWithError(req, resp, "This file is no longer available for download.");
                return;
            }

            StorageService.StoredFile storedFile = StorageService.getInstance().download(note.getFilePath());

            resp.setContentType(storedFile.getContentType());
            resp.setHeader("Content-Disposition", "attachment; filename=\"" + storedFile.getFileName() + "\"");
            resp.setContentLength(storedFile.getBytes().length);
            resp.getOutputStream().write(storedFile.getBytes());
        } catch (SQLException e) {
            e.printStackTrace();
            redirectWithError(req, resp, "A database error prevented the download. Please try again later.");
        } catch (RuntimeException e) {
            // Storage failures (missing object, unreachable endpoint, bad credentials,
            // etc.) surface as unchecked exceptions. Show a friendly banner on the page
            // the user came from instead of a raw HTTP 500 error page.
            e.printStackTrace();
            redirectWithError(req, resp, "This file could not be downloaded right now. Please try again later.");
        }
    }

    /**
     * Redirects back to the page the user came from (falling back to the home
     * page) with a URL-encoded {@code error} parameter, so the existing
     * ".feedback error" banner displays the message - mirroring the upload flow.
     */
    private void redirectWithError(HttpServletRequest req, HttpServletResponse resp, String message)
            throws IOException {
        String referer = req.getHeader("Referer");
        String target;
        if (referer != null && !referer.isBlank()) {
            // Drop any existing query string so old error/success params don't pile up.
            int queryStart = referer.indexOf('?');
            target = queryStart >= 0 ? referer.substring(0, queryStart) : referer;
        } else {
            target = req.getContextPath() + "/jsp/home.jsp";
        }

        String separator = target.contains("?") ? "&" : "?";
        resp.sendRedirect(target + separator + "error="
                + URLEncoder.encode(message, StandardCharsets.UTF_8));
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
