package dao.favorite;

import dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Counts how many users have favorited a note.
 * Output: the number of favorites.
 */
public final class GetFavoriteCountByNoteIdDAO extends AbstractDAO<Integer> {

    private static final String SQL =
            "SELECT COUNT(*) AS count FROM favorite WHERE note_id = ?";

    private final int noteId;

    public GetFavoriteCountByNoteIdDAO(final Connection con, final int noteId) {
        super(con);
        this.noteId = noteId;
    }

    @Override
    protected void doAccess() throws SQLException {
        int count = 0;
        try (PreparedStatement ps = con.prepareStatement(SQL)) {
            ps.setInt(1, noteId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt("count");
                }
            }
        }
        outputParam = count;
    }
}
