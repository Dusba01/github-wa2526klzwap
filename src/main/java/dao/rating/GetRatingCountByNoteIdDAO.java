package dao.rating;

import dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Counts how many ratings a note has received. Output: the count.
 */
public final class GetRatingCountByNoteIdDAO extends AbstractDAO<Integer> {

    private static final String SQL =
            "SELECT COUNT(*) AS count FROM rating WHERE note_id = ?";

    private final int noteId;

    public GetRatingCountByNoteIdDAO(final Connection con, final int noteId) {
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
