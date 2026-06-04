package controller.User;

import controller.AbstractDatabaseServlet;
import jakarta.servlet.ServletException;
import model.User;
import dao.user.CheckLoginDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginServlet extends AbstractDatabaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/jsp/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String credential = req.getParameter("credential");
        String password = req.getParameter("password");

        try {
            User user = new CheckLoginDAO(getConnection(), credential, password).access().getOutputParam();

            if (user != null) {
                req.getSession().setAttribute("user", user);
                resp.sendRedirect(req.getContextPath() + "/jsp/home.jsp");
            } else {
                resp.sendRedirect(req.getContextPath() + "/jsp/login.jsp?error");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
