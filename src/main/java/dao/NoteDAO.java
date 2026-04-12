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

    public static List<Note> getNotesByAuthorId(int authorId) throws SQLException {
        String sql = "SELECT id, author_id, course_id, title, description, upload_date, file_path FROM note WHERE author_id = ? ORDER BY upload_date DESC";
        List<Note> notes = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, authorId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    notes.add(mapNote(rs));
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
}
