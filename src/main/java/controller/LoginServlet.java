package controller;

import jakarta.servlet.ServletException;
import model.User;
import dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/jsp/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String credential = req.getParameter("credential");
        String password = req.getParameter("password");

        // stampa per verificare che il form arrivi alla servlet
        System.out.println("Tentativo di login con username/email: " + credential);

        try {
            // stampa per vedere se la connessione e la query funzionano
            User user = UserDAO.checkLogin(credential, password);
            System.out.println("Risultato checkLogin: " + user);

            if (user != null) {
                req.getSession().setAttribute("user", user);
                System.out.println("Login riuscito, redirect verso home.jsp");
                resp.sendRedirect("jsp/home.jsp"); // nuova JSP
            } else {
                System.out.println("Credenziali non valide, redirect verso login.jsp?error=1");
                resp.sendRedirect("jsp/login.jsp?error=1");
            }
        } catch (SQLException e) {
            System.out.println("Errore SQL durante il login:");
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
