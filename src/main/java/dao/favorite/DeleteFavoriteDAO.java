package dao.favorite;

import dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Removes a note from a user's favorites. Output: {@code true} if a row was
 * deleted, {@code false} otherwise.
 */
public final class DeleteFavoriteDAO extends AbstractDAO<Boolean> {

    private static final String SQL =
            "DELETE FROM favorite WHERE user_id = ? AND note_id = ?";

    private final int userId;
    private final int noteId;

    public DeleteFavoriteDAO(final Connection con, final int userId, final int noteId) {
        super(con);
        this.userId = userId;
        this.noteId = noteId;
    }

    @Override
    protected void doAccess() throws SQLException {
        int affected;
        try (PreparedStatement ps = con.prepareStatement(SQL)) {
            ps.setInt(1, userId);
            ps.setInt(2, noteId);
            affected = ps.executeUpdate();
        }
        outputParam = affected > 0;
    }
}
