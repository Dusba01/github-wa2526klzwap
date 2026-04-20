package dao;

import model.Course;
import utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO {

    public static class CourseSummary {
        private final int id;
        private final String name;
        private final int documentCount;

        public CourseSummary(int id, String name, int documentCount) {
            this.id = id;
            this.name = name;
            this.documentCount = documentCount;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public int getDocumentCount() {
            return documentCount;
        }
    }

    public static Course saveCourse(Course course) throws SQLException {
        String sql = "INSERT INTO course (name) VALUES (?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, course.getName());
            ps.executeUpdate();

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    course.setId(generatedKeys.getInt(1));
                }
            }

            return course;
        }
    }

    public static List<Course> getAllCourses() throws SQLException {
        String sql = "SELECT id, name FROM course ORDER BY name ASC";
        List<Course> courses = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                courses.add(mapCourse(rs));
            }
        }

        return courses;
    }

    public static List<CourseSummary> getCourseSummaries() throws SQLException {
        String sql =
                "SELECT c.id, c.name, COUNT(n.id) AS document_count " +
                        "FROM course c " +
                        "LEFT JOIN note n ON n.course_id = c.id " +
                        "GROUP BY c.id, c.name " +
                        "ORDER BY document_count DESC, c.name ASC";
        List<CourseSummary> summaries = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                summaries.add(new CourseSummary(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("document_count")
                ));
            }
        }

        return summaries;
    }

    private static Course mapCourse(ResultSet rs) throws SQLException {
        Course course = new Course();
        course.setId(rs.getInt("id"));
        course.setName(rs.getString("name"));
        return course;
    }
}
