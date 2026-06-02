package dao.favorite;

import dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Tells whether a note is among a user's favorites.
 * Output: {@code true} if it is, {@code false} otherwise.
 */
public final class IsFavoriteDAO extends AbstractDAO<Boolean> {

    private static final String SQL =
            "SELECT 1 FROM favorite WHERE user_id = ? AND note_id = ?";

    private final int userId;
    private final int noteId;

    public IsFavoriteDAO(final Connection con, final int userId, final int noteId) {
        super(con);
        this.userId = userId;
        this.noteId = noteId;
    }

    @Override
    protected void doAccess() throws SQLException {
        boolean isFavorite = false;
        try (PreparedStatement ps = con.prepareStatement(SQL)) {
            ps.setInt(1, userId);
            ps.setInt(2, noteId);
            try (ResultSet rs = ps.executeQuery()) {
                isFavorite = rs.next();
            }
        }
        outputParam = isFavorite;
    }
}
