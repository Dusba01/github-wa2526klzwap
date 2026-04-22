package controller;

import jakarta.servlet.ServletException;
import dao.UserDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.User;

import java.io.IOException;
import java.sql.SQLException;
import java.util.regex.Pattern;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{7,15}$");

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String name = trim(req.getParameter("name"));
        String username = trim(req.getParameter("username"));
        String email = trim(req.getParameter("email"));
        String password = req.getParameter("password");

        if (name == null || username == null || email == null || password == null || password.isBlank()) {
            forwardWithError(req, resp, "All fields are required.");
            return;
        }

        if (!email.toLowerCase().endsWith("@studenti.unipd.it")) {
            forwardWithError(req, resp, "Registration is allowed only with @studenti.unipd.it email addresses.");
            return;
        }

        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            forwardWithError(req, resp, "Password must be 7 to 15 characters long and contain at least one uppercase letter, one lowercase letter, and one number.");
            return;
        }

        try {
            if (UserDAO.usernameExists(username)) {
                forwardWithError(req, resp, "This username is already taken.");
                return;
            }

            if (UserDAO.emailExists(email)) {
                forwardWithError(req, resp, "This email address is already registered.");
                return;
            }

            User user = new User(name, username, email, password);
            UserDAO.insertUser(user);
            resp.sendRedirect(req.getContextPath() + "/jsp/login.jsp");
        } catch (SQLException e) {
            e.printStackTrace();
            forwardWithError(req, resp, "Registration failed. Please verify your data and try again.");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.getRequestDispatcher("/jsp/register.jsp").forward(req, resp);
    }

    private void forwardWithError(HttpServletRequest req, HttpServletResponse resp, String errorMessage)
            throws ServletException, IOException {
        req.setAttribute("error", errorMessage);
        req.setAttribute("nameValue", req.getParameter("name"));
        req.setAttribute("usernameValue", req.getParameter("username"));
        req.setAttribute("emailValue", req.getParameter("email"));
        req.getRequestDispatcher("/jsp/register.jsp").forward(req, resp);
    }

    private String trim(String value) {
        if (value == null) {
            return null;
        }

        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
