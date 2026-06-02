package dao.course;

import dao.AbstractDAO;
import model.Course;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Lists all courses ordered by name. Output list: the {@link Course}s.
 */
public final class ListCoursesDAO extends AbstractDAO<Course> {

    private static final String SQL = "SELECT id, name FROM course ORDER BY name ASC";

    public ListCoursesDAO(final Connection con) {
        super(con);
    }

    @Override
    protected void doAccess() throws SQLException {
        final List<Course> courses = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(SQL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                final Course course = new Course();
                course.setId(rs.getInt("id"));
                course.setName(rs.getString("name"));
                courses.add(course);
            }
        }
        outputListParam = courses;
    }
}
