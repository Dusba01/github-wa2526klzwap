package controller.Rating;

import com.fasterxml.jackson.core.JsonGenerator;
import dao.RatingDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Rating;
import model.User;

import java.io.IOException;
import java.sql.SQLException;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

@WebServlet("/rest/ratings/*")
public class RatingServlet extends HttpServlet {

    private static final JsonFactory JSON_FACTORY = new JsonFactory();

    //inserisce o aggiorna voto
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws IOException {

        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");

        try {
            User user = (User) req.getSession().getAttribute("user");

            if (user == null) {
                res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                res.getWriter().write("{\"error\": \"Utente non autenticato\"}");
                return;
            }

            String path = req.getPathInfo(); // /5
            if (path == null || path.equals("/")) {
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                res.getWriter().write("{\"error\":\"noteId mancante\"}");
                return;
            }

            int userId = user.getId();
            int noteId = Integer.parseInt(path.substring(1));

            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(req.getInputStream());
            int value = node.get("value").asInt();

            if (value < 1 || value > 5) {
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                res.getWriter().write("{\"error\": \"Valore non valido\"}");
                return;
            }

            Rating rating = new Rating(userId, noteId, value);
            RatingDAO.saveRating(rating);

            res.setStatus(HttpServletResponse.SC_OK);
            res.getWriter().write("{\"message\": \"Rating salvato\"}");

        } catch (NumberFormatException e) {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            res.getWriter().write("{\"error\": \"Parametri non validi\"}");

        } catch (SQLException e) {
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            res.getWriter().write("{\"error\": \"Errore database\"}");
            e.printStackTrace();
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse res)
            throws IOException {

        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");

        try {
            User user = (User) req.getSession().getAttribute("user");

            if (user == null) {
                res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                res.getWriter().write("{\"error\": \"Utente non autenticato\"}");
                return;
            }

            String path = req.getPathInfo(); // /5
            if (path == null || path.equals("/")) {
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                res.getWriter().write("{\"error\":\"noteId mancante\"}");
                return;
            }

            int userId = user.getId();
            int noteId = Integer.parseInt(path.substring(1));

            RatingDAO.deleteRating(userId, noteId);

            res.setStatus(HttpServletResponse.SC_OK);
            res.getWriter().write("{\"message\": \"Rating rimosso\"}");

        } catch (NumberFormatException e) {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            res.getWriter().write("{\"error\": \"Parametri non validi\"}");

        } catch (SQLException e) {
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            res.getWriter().write("{\"error\": \"Errore database\"}");
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws IOException {

        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");

        try {
            // /rest/ratings/info/5
            String path = req.getPathInfo(); // "/5"

            if (path == null || path.equals("/")) {
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                res.getWriter().write("{\"error\":\"noteId mancante nel path\"}");
                return;
            }

            int noteId = Integer.parseInt(path.substring(1));

            User user = (User) req.getSession().getAttribute("user");
            Integer userId = (user != null) ? user.getId() : null;

            double average = RatingDAO.getAverageRatingByNoteId(noteId);
            int count = RatingDAO.getRatingCountByNoteId(noteId);

            Integer userValue = null;

            if (userId != null) {
                var r = RatingDAO.getRatingByUserAndNote(userId, noteId);
                if (r != null) {
                    userValue = r.getValue();
                }
            }

            res.setStatus(HttpServletResponse.SC_OK);

            JsonGenerator jg = JSON_FACTORY.createGenerator(res.getOutputStream());

            jg.writeStartObject();

            jg.writeNumberField("average", average);
            jg.writeNumberField("count", count);

            if (userValue == null) {
                jg.writeNullField("userValue");
            } else {
                jg.writeNumberField("userValue", userValue);
            }

            jg.writeEndObject();

            jg.flush();

        } catch (NumberFormatException e) {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            res.getWriter().write("{\"error\":\"noteId non valido\"}");

        } catch (SQLException e) {
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            res.getWriter().write("{\"error\":\"Errore database\"}");
            e.printStackTrace();
        }
    }

}
