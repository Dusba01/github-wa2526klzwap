package dao;

import model.Note;
import utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class NoteDAO {

    public static Note saveNote(Note note) throws SQLException {
        String sql = "INSERT INTO note (author_id, course_id, title, description, file_path) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, note.getAuthorId());
            ps.setInt(2, note.getCourseId());
            ps.setString(3, note.getTitle());
            ps.setString(4, note.getDescription());
            ps.setString(5, note.getFilePath());
            ps.executeUpdate();

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    note.setId(generatedKeys.getInt(1));
                }
            }

            return note;
        }
    }

    public static Note getNoteById(int id) throws SQLException {
        String sql = "SELECT id, author_id, course_id, title, description, upload_date, file_path FROM note WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapNote(rs);
                }
            }
        }

        return null;
    }

    public static Note getNoteByIdAndAuthorId(int id, int authorId) throws SQLException {
        String sql = "SELECT id, author_id, course_id, title, description, upload_date, file_path FROM note WHERE id = ? AND author_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setInt(2, authorId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapNote(rs);
                }
            }
        }

        return null;
    }

    public static List<Note> getAllNotes() throws SQLException {
        String sql = "SELECT id, author_id, course_id, title, description, upload_date, file_path FROM note ORDER BY upload_date DESC";
        List<Note> notes = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                notes.add(mapNote(rs));
            }
        }

        return notes;
    }

    public static boolean deleteNoteByIdAndAuthorId(int id, int authorId) throws SQLException {
        String sql = "DELETE FROM note WHERE id = ? AND author_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setInt(2, authorId);
            return ps.executeUpdate() > 0;
        }
    }

    public static List<Note> getNotesByAuthorId(int authorId) throws SQLException {
        String sql =
                "SELECT n.id, n.author_id, n.course_id, n.title, n.description, n.upload_date, n.file_path, " +
                        "       c.name AS course_name, u.username AS author_username " +
                        "FROM note n " +
                        "JOIN course c ON n.course_id = c.id " +
                        "JOIN users u ON n.author_id = u.id " +
                        "WHERE n.author_id = ? " +
                        "ORDER BY n.upload_date DESC";
        List<Note> notes = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, authorId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    notes.add(mapNoteWithDetails(rs));
                }
            }
        }

        return notes;
    }

    public static List<Note> getNotesByCourseId(int courseId) throws SQLException {
        String sql = "SELECT id, author_id, course_id, title, description, upload_date, file_path FROM note WHERE course_id = ? ORDER BY upload_date DESC";
        List<Note> notes = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, courseId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    notes.add(mapNote(rs));
                }
            }
        }

        return notes;
    }

    public static List<Note> searchNotes(String query) throws SQLException {
        String sql =
                "SELECT n.id, n.title, n.description, n.upload_date, n.file_path, " +
                        "       n.course_id, n.author_id, " +
                        "       c.name AS course_name, u.username AS author_username " +
                        "FROM note n " +
                        "JOIN course c ON n.course_id = c.id " +
                        "JOIN users u ON n.author_id = u.id " +
                        "WHERE LOWER(n.title) LIKE ? OR LOWER(n.description) LIKE ? " +
                        "OR LOWER(c.name) LIKE ? OR LOWER(u.username) LIKE ? " +
                        "ORDER BY n.upload_date DESC";
        List<Note> notes = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String likeQuery = "%" + query.toLowerCase() + "%";

            ps.setString(1, likeQuery);
            ps.setString(2, likeQuery);
            ps.setString(3, likeQuery);
            ps.setString(4, likeQuery);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    notes.add(mapNoteWithDetails(rs));
                }
            }
        }

        return notes;
    }

    private static Note mapNote(ResultSet rs) throws SQLException {
        Note note = new Note();
        note.setId(rs.getInt("id"));
        note.setAuthorId(rs.getInt("author_id"));
        note.setCourseId(rs.getInt("course_id"));
        note.setTitle(rs.getString("title"));
        note.setDescription(rs.getString("description"));
        note.setFilePath(rs.getString("file_path"));

        Timestamp uploadDate = rs.getTimestamp("upload_date");
        if (uploadDate != null) {
            note.setUploadDate(uploadDate.toLocalDateTime());
        }

        return note;
    }

    private static Note mapNoteWithDetails(ResultSet rs) throws SQLException {

        // riuso del mapping base
        Note note = mapNote(rs);

        // aggiungo SOLO i campi extra della JOIN
        note.setCourseName(rs.getString("course_name"));
        note.setAuthorUsername(rs.getString("author_username"));

        return note;
    }
}
