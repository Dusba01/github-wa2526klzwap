package controller.Favorite;

import dao.NoteDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/favorites")
public class FavoritesPageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // AuthenticationFilter guarantees an authenticated user in the session.
        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("user");

        try {
            req.setAttribute("favoriteNotes", NoteDAO.getFavoriteNotesByUserId(user.getId()));
            req.getRequestDispatcher("/jsp/favorites.jsp").forward(req, resp);
        } catch (SQLException e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/jsp/home.jsp?error=Unable to load favorites.");
        }
    }
}
