package dao.favorite;

import dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Removes every favorite pointing at a given note (used when a note is deleted).
 * Output: the number of removed favorites.
 */
public final class DeleteFavoritesByNoteIdDAO extends AbstractDAO<Integer> {

    private static final String SQL = "DELETE FROM favorite WHERE note_id = ?";

    private final int noteId;

    public DeleteFavoritesByNoteIdDAO(final Connection con, final int noteId) {
        super(con);
        this.noteId = noteId;
    }

    @Override
    protected void doAccess() throws SQLException {
        int affected;
        try (PreparedStatement ps = con.prepareStatement(SQL)) {
            ps.setInt(1, noteId);
            affected = ps.executeUpdate();
        }
        outputParam = affected;
    }
}
