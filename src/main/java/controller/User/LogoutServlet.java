package controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        System.out.println("Attempting logout for the current user");

        if (req.getSession(false) != null) {
            req.getSession(false).invalidate();
            System.out.println("Logout successful, session closed");
        } else {
            System.out.println("No active session to invalidate");
        }

        System.out.println("Redirecting to login.jsp");
        resp.sendRedirect("jsp/login.jsp");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        doPost(req, resp);
    }
}
