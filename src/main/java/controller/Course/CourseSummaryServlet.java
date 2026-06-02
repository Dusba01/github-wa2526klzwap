package controller.Course;

import controller.AbstractDatabaseServlet;
import dao.course.ListCourseSummariesDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.CourseSummary;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;

@WebServlet("/rest/courses/summary")
public class CourseSummaryServlet extends AbstractDatabaseServlet {

    private static final JsonFactory JSON_FACTORY = new JsonFactory();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");

        try {
            List<CourseSummary> courses =
                    new ListCourseSummariesDAO(getConnection()).access().getOutputListParam();

            JsonGenerator jg = JSON_FACTORY.createGenerator(res.getOutputStream());
            jg.writeStartArray();

            for (CourseSummary course : courses) {
                jg.writeStartObject();
                jg.writeNumberField("id", course.getId());
                jg.writeStringField("name", course.getName());
                jg.writeNumberField("documentCount", course.getDocumentCount());
                jg.writeEndObject();
            }

            jg.writeEndArray();
            jg.flush();
        } catch (SQLException e) {
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            res.getWriter().write("{\"error\":\"Database error\"}");
            e.printStackTrace();
        }
    }
}
