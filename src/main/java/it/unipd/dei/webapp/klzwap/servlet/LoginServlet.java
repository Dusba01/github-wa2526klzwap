package it.unipd.dei.webapp.klzwap.servlet;

import it.unipd.dei.webapp.klzwap.dao.CreateUserDAO;
import it.unipd.dei.webapp.klzwap.dao.LoginUserDAO;
import it.unipd.dei.webapp.klzwap.model.User;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "LoginServlet", urlPatterns = {"/login", "/register"})
public final class LoginServlet extends HttpServlet {

    private static final String STUDENT_EMAIL_SUFFIX = "@studenti.unipd.it";
    private static final String PASSWORD_PATTERN =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z\\d]).{8,}$";
    private static final String DB_DRIVER_PARAM = "dbDriver";
    private static final String DB_URL_PARAM = "dbUrl";
    private static final String DB_USER_PARAM = "dbUser";
    private static final String DB_PASSWORD_PARAM = "dbPassword";
    private static final String DEFAULT_DB_DRIVER = "org.postgresql.Driver";
    private static final String DEFAULT_DB_URL = "jdbc:postgresql://localhost:5432/klz_wap";
    private static final String AUTHENTICATED_USER = "authenticatedUser";
    private static final String HOME_PAGE = "/jsp/home.jsp";

    private String dbUrl;
    private String dbUser;
    private String dbPassword;

    @Override
    public void init() throws ServletException {
        final String dbDriver = readContextParam(DB_DRIVER_PARAM, DEFAULT_DB_DRIVER);
        dbUrl = readContextParam(DB_URL_PARAM, DEFAULT_DB_URL);
        dbUser = getServletContext().getInitParameter(DB_USER_PARAM);
        dbPassword = getServletContext().getInitParameter(DB_PASSWORD_PARAM);

        if (dbUser == null || dbUser.trim().isEmpty()) {
            throw new ServletException("Missing servlet context parameter: " + DB_USER_PARAM);
        }

        if (dbPassword == null) {
            dbPassword = "";
        }

        try {
            Class.forName(dbDriver);
        } catch (ClassNotFoundException e) {
            throw new ServletException("Unable to load database driver: " + dbDriver, e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        final String operation = extractOperation(req);

        switch (operation) {
            case "login":
                showLoginPage(req, res);
                break;
            case "register":
                showRegisterPage(req, res);
                break;
            default:
                res.sendError(HttpServletResponse.SC_NOT_FOUND, "Operation Unknown");
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        final String operation = extractOperation(req);

        switch (operation) {
            case "login":
                loginOperations(req, res);
                break;
            case "register":
                registrationOperations(req, res);
                break;
            default:
                res.sendError(HttpServletResponse.SC_NOT_FOUND, "Operation Unknown");
                break;
        }
    }

    private void loginOperations(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        final String usernameOrEmail = trimToNull(req.getParameter("usernameOrEmail"));
        final String password = trimToNull(req.getParameter("password"));

        if (usernameOrEmail == null || password == null) {
            res.sendRedirect(req.getContextPath() + "/login?error=missing-credentials");
            return;
        }

        try (Connection connection = getConnection()) {
            final boolean authenticated =
                    new LoginUserDAO(connection, usernameOrEmail, password).access().getOutputParam();

            if (!authenticated) {
                res.sendRedirect(req.getContextPath() + "/login?error=invalid-credentials");
                return;
            }

            final HttpSession session = req.getSession(true);
            session.setAttribute(AUTHENTICATED_USER, usernameOrEmail);
            res.sendRedirect(req.getContextPath() + HOME_PAGE);
        } catch (SQLException e) {
            throw new ServletException("Authentication request failed.", e);
        }
    }

    private void registrationOperations(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        final String username = trimToNull(req.getParameter("username"));
        final String email = trimToNull(req.getParameter("email"));
        final String password = trimToNull(req.getParameter("password"));

        if (username == null || email == null || password == null) {
            res.sendRedirect(req.getContextPath() + "/register?error=missing-fields");
            return;
        }

        if (!email.toLowerCase().endsWith(STUDENT_EMAIL_SUFFIX)) {
            res.sendRedirect(req.getContextPath() + "/register?error=invalid-email");
            return;
        }

        if (!password.matches(PASSWORD_PATTERN)) {
            res.sendRedirect(req.getContextPath() + "/register?error=weak-password");
            return;
        }

        final User user = new User(username, password, email);

        try (Connection connection = getConnection()) {
            final boolean created = new CreateUserDAO(connection, user).access().getOutputParam();

            if (created) {
                res.sendRedirect(req.getContextPath() + "/login?registered=true");
                return;
            }

            res.sendRedirect(req.getContextPath() + "/register?error=registration-failed");
        } catch (SQLException e) {
            if (isDuplicateKeyViolation(e)) {
                res.sendRedirect(req.getContextPath() + "/register?error=user-exists");
                return;
            }

            throw new ServletException("Registration request failed.", e);
        }
    }

    private void showLoginPage(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        final HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute(AUTHENTICATED_USER) != null) {
            res.sendRedirect(req.getContextPath() + HOME_PAGE);
            return;
        }

        req.getRequestDispatcher("/jsp/login.jsp").forward(req, res);
    }

    private void showRegisterPage(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        req.getRequestDispatcher("/jsp/register.jsp").forward(req, res);
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    private String extractOperation(HttpServletRequest req) {
        final String requestUri = req.getRequestURI();
        return requestUri.substring(requestUri.lastIndexOf('/') + 1);
    }

    private String readContextParam(String name, String defaultValue) {
        final String value = getServletContext().getInitParameter(name);
        return value != null && !value.trim().isEmpty() ? value : defaultValue;
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }

        final String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private boolean isDuplicateKeyViolation(SQLException exception) {
        return "23505".equals(exception.getSQLState());
    }
}
