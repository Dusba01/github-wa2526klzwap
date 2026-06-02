package dao.course;

import dao.AbstractDAO;
import model.Course;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Inserts a new course and fills in its generated id.
 * Output: the stored {@link Course}.
 */
public final class CreateCourseDAO extends AbstractDAO<Course> {

    private static final String SQL = "INSERT INTO course (name) VALUES (?)";

    private final Course course;

    public CreateCourseDAO(final Connection con, final Course course) {
        super(con);
        this.course = course;
    }

    @Override
    protected void doAccess() throws SQLException {
        try (PreparedStatement ps =
                     con.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, course.getName());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    course.setId(keys.getInt(1));
                }
            }
        }
        outputParam = course;
    }
}
