package controller;

import dao.FavoriteDAO;
import dao.NoteDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Note;
import model.User;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import com.fasterxml.jackson.core.*;

@WebServlet("/rest/notes/search")
public class SearchNoteServlet extends HttpServlet {

    private static final JsonFactory JSON_FACTORY = new JsonFactory();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws IOException {

        String query = req.getParameter("query");

        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");

        if (query == null || query.isBlank()) {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            res.getWriter().write("{\"error\": \"Query mancante\"}");
            return;
        }

        try {
            List<Note> notes = NoteDAO.searchNotes(query);
            User user = (User) req.getSession().getAttribute("user");

            res.setStatus(HttpServletResponse.SC_OK);

            JsonGenerator jg = JSON_FACTORY.createGenerator(res.getOutputStream());

            jg.writeStartArray(); // [

            for (int i = 0; i < notes.size(); i++) {
                Note n = notes.get(i);

                jg.writeStartObject(); // {

                jg.writeNumberField("id", n.getId());
                jg.writeStringField("title", n.getTitle());
                jg.writeStringField("description", n.getDescription());
                jg.writeNumberField("courseId", n.getCourseId());
                jg.writeNumberField("authorId", n.getAuthorId());

                // campi "extra"
                jg.writeStringField("courseName", n.getCourseName());
                jg.writeStringField("authorUsername", n.getAuthorUsername());
                jg.writeBooleanField("isFavorite",
                        user != null && FavoriteDAO.isFavorite(user.getId(), n.getId()));

                jg.writeEndObject(); // }
            }

            jg.writeEndArray(); // ]

            jg.flush();

        } catch (SQLException e) {
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            res.getWriter().write("{\"error\": \"Errore database\"}");
            e.printStackTrace();
        }
    }

}
