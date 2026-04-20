package dao;

import model.Favorite;
import utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class FavoriteDAO {

    public static void saveFavorite(Favorite favorite) throws SQLException {
        String sql =
                "INSERT INTO favorite (user_id, note_id) " +
                        "VALUES (?, ?) " +
                        "ON CONFLICT (user_id, note_id) DO NOTHING";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, favorite.getUserId());
            ps.setInt(2, favorite.getNoteId());
            ps.executeUpdate();

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    favorite.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    public static void deleteFavorite(int userId, int noteId) throws SQLException {
        String sql = "DELETE FROM favorite WHERE user_id = ? AND note_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, noteId);
            ps.executeUpdate();
        }
    }

    public static void deleteFavoritesByNoteId(int noteId) throws SQLException {
        String sql = "DELETE FROM favorite WHERE note_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, noteId);
            ps.executeUpdate();
        }
    }

    public static Favorite getFavoriteByUserAndNote(int userId, int noteId) throws SQLException {
        String sql = "SELECT id, user_id, note_id, created_at FROM favorite WHERE user_id = ? AND note_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, noteId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapFavorite(rs);
                }
            }
        }

        return null;
    }

    public static boolean isFavorite(int userId, int noteId) throws SQLException {
        return getFavoriteByUserAndNote(userId, noteId) != null;
    }

    public static int getFavoriteCountByNoteId(int noteId) throws SQLException {
        String sql = "SELECT COUNT(*) AS count FROM favorite WHERE note_id = ?";

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

    public static List<Favorite> getFavoritesByUserId(int userId) throws SQLException {
        String sql = "SELECT id, user_id, note_id, created_at FROM favorite WHERE user_id = ? ORDER BY created_at DESC";
        List<Favorite> favorites = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    favorites.add(mapFavorite(rs));
                }
            }
        }

        return favorites;
    }

    public static List<Favorite> getFavoritesByNoteId(int noteId) throws SQLException {
        String sql = "SELECT id, user_id, note_id, created_at FROM favorite WHERE note_id = ? ORDER BY created_at DESC";
        List<Favorite> favorites = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, noteId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    favorites.add(mapFavorite(rs));
                }
            }
        }

        return favorites;
    }

    private static Favorite mapFavorite(ResultSet rs) throws SQLException {
        Favorite favorite = new Favorite();
        favorite.setId(rs.getInt("id"));
        favorite.setUserId(rs.getInt("user_id"));
        favorite.setNoteId(rs.getInt("note_id"));

        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            favorite.setCreatedAt(createdAt.toLocalDateTime());
        }

        return favorite;
    }
}
