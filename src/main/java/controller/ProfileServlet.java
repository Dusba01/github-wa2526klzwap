package controller;

import dao.CourseDAO;
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

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = session != null ? (User) session.getAttribute("user") : null;

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/jsp/login.jsp?error=login_required");
            return;
        }

        try {
            req.setAttribute("userProfile", user);
            req.setAttribute("courses", CourseDAO.getAllCourses());
            req.setAttribute("uploadedNotes", NoteDAO.getNotesByAuthorId(user.getId()));
            req.getRequestDispatcher("/jsp/profile.jsp").forward(req, resp);
        } catch (SQLException e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/jsp/home.jsp?error=Unable to load profile.");
        }
    }
}
