package dao;

import model.Rating;
import utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class RatingDAO {

    //INSERT oppure UPDATE (se già esiste)
    public static void saveRating(Rating rating) throws SQLException {
        String sql =
                "INSERT INTO rating (user_id, note_id, value) " +
                        "VALUES (?, ?, ?) " +
                        "ON CONFLICT (user_id, note_id) " +
                        "DO UPDATE SET value = EXCLUDED.value";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, rating.getUserId());
            ps.setInt(2, rating.getNoteId());
            ps.setInt(3, rating.getValue());
            ps.executeUpdate();
        }
    }

    // elimina voto (click sulla stessa stella assegnata in precedenza)
    public static void deleteRating(int userId, int noteId) throws SQLException {

        String sql = "DELETE FROM rating WHERE user_id = ? AND note_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, noteId);
            ps.executeUpdate();
        }
    }

    public static void deleteRatingsByNoteId(int noteId) throws SQLException {
        String sql = "DELETE FROM rating WHERE note_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, noteId);
            ps.executeUpdate();
        }
    }

    public static Rating getRatingByUserAndNote(int userId, int noteId) throws SQLException {

        String sql = "SELECT id, user_id, note_id, value, created_at " +
                "FROM rating WHERE user_id = ? AND note_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, noteId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRating(rs);
                }
            }
        }

        return null;
    }

    public static double getAverageRatingByNoteId(int noteId) throws SQLException {

        String sql = "SELECT AVG(value) AS avg_rating FROM rating WHERE note_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, noteId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("avg_rating"); // 0.0 se NULL
                }
            }
        }

        return 0.0;
    }

    public static int getRatingCountByNoteId(int noteId) throws SQLException {

        String sql = "SELECT COUNT(*) AS count FROM rating WHERE note_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, noteId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("count");
                }
            }
        }

        return 0;
    }

    public static List<Rating> getRatingsByUserId(int userId) throws SQLException {
        String sql = "SELECT id, user_id, note_id, value, created_at FROM rating WHERE user_id = ? ORDER BY created_at DESC";
        List<Rating> ratings = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ratings.add(mapRating(rs));
                }
            }
        }

        return ratings;
    }

    public static List<Rating> getRatingsByNoteId(int noteId) throws SQLException {
        String sql = "SELECT id, user_id, note_id, value, created_at FROM rating WHERE note_id = ? ORDER BY created_at DESC";
        List<Rating> ratings = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, noteId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ratings.add(mapRating(rs));
                }
            }
        }

        return ratings;
    }

    private static Rating mapRating(ResultSet rs) throws SQLException {
        Rating rating = new Rating();
        rating.setId(rs.getInt("id"));
        rating.setUserId(rs.getInt("user_id"));
        rating.setNoteId(rs.getInt("note_id"));
        rating.setValue(rs.getInt("value"));

        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            rating.setCreatedAt(createdAt.toLocalDateTime());
        }

        return rating;
    }
}
