package controller.Favorite;

import dao.FavoriteDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

@WebServlet("/remove-favorite")
public class RemoveFavoriteServlet extends HttpServlet {

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
            resp.sendRedirect(req.getContextPath() + "/favorites?error="
                    + URLEncoder.encode("Invalid favorite selected.", StandardCharsets.UTF_8));
            return;
        }

        try {
            FavoriteDAO.deleteFavorite(user.getId(), noteId);
            resp.sendRedirect(req.getContextPath() + "/favorites");
        } catch (SQLException e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/favorites?error="
                    + URLEncoder.encode("Unable to remove favorite.", StandardCharsets.UTF_8));
        }
    }

    private Integer parseNoteId(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        try {
            int noteId = Integer.parseInt(value);
            return noteId > 0 ? noteId : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
