package dao.rating;

import dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Removes every rating of a given note (used when a note is deleted).
 * Output: the number of removed ratings.
 */
public final class DeleteRatingsByNoteIdDAO extends AbstractDAO<Integer> {

    private static final String SQL = "DELETE FROM rating WHERE note_id = ?";

    private final int noteId;

    public DeleteRatingsByNoteIdDAO(final Connection con, final int noteId) {
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
