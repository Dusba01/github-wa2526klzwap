package dao.favorite;

import dao.AbstractDAO;
import model.Favorite;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Reads the favorite linking a user and a note.
 * Output: the {@link Favorite}, or {@code null} if absent.
 */
public final class GetFavoriteByUserAndNoteDAO extends AbstractDAO<Favorite> {

    private static final String SQL =
            "SELECT id, user_id, note_id, created_at FROM favorite "
                    + "WHERE user_id = ? AND note_id = ?";

    private final int userId;
    private final int noteId;

    public GetFavoriteByUserAndNoteDAO(final Connection con, final int userId, final int noteId) {
        super(con);
        this.userId = userId;
        this.noteId = noteId;
    }

    @Override
    protected void doAccess() throws SQLException {
        Favorite favorite = null;
        try (PreparedStatement ps = con.prepareStatement(SQL)) {
            ps.setInt(1, userId);
            ps.setInt(2, noteId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    favorite = FavoriteMapper.map(rs);
                }
            }
        }
        outputParam = favorite;
    }
}
