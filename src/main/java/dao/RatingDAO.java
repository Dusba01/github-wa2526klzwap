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

    public static Rating saveRating(Rating rating) throws SQLException {
        String sql = "INSERT INTO rating (user_id, note_id, value) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, rating.getUserId());
            ps.setInt(2, rating.getNoteId());
            ps.setInt(3, rating.getValue());
            ps.executeUpdate();

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    rating.setId(generatedKeys.getInt(1));
                }
            }

            return rating;
        }
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
