package controller.Favorite;

import dao.FavoriteDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Favorite;
import model.User;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/rest/favorites/*")
public class FavoriteServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        User user = (User) req.getSession().getAttribute("user");

        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");

        if (user == null) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.getWriter().write("{\"error\":\"User not authenticated\"}");
            return;
        }

        Integer noteId = parseNoteId(req.getPathInfo());
        if (noteId == null) {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            res.getWriter().write("{\"error\":\"Invalid noteId\"}");
            return;
        }

        try {
            FavoriteDAO.saveFavorite(new Favorite(user.getId(), noteId));
            res.setStatus(HttpServletResponse.SC_OK);
            res.getWriter().write("{\"favorite\":true}");
        } catch (SQLException e) {
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            res.getWriter().write("{\"error\":\"Database error\"}");
            e.printStackTrace();
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse res) throws IOException {
        User user = (User) req.getSession().getAttribute("user");

        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");

        if (user == null) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.getWriter().write("{\"error\":\"User not authenticated\"}");
            return;
        }

        Integer noteId = parseNoteId(req.getPathInfo());
        if (noteId == null) {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            res.getWriter().write("{\"error\":\"Invalid noteId\"}");
            return;
        }

        try {
            FavoriteDAO.deleteFavorite(user.getId(), noteId);
            res.setStatus(HttpServletResponse.SC_OK);
            res.getWriter().write("{\"favorite\":false}");
        } catch (SQLException e) {
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            res.getWriter().write("{\"error\":\"Database error\"}");
            e.printStackTrace();
        }
    }

    private Integer parseNoteId(String pathInfo) {
        if (pathInfo == null || pathInfo.equals("/")) {
            return null;
        }

        try {
            int noteId = Integer.parseInt(pathInfo.substring(1));
            return noteId > 0 ? noteId : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
