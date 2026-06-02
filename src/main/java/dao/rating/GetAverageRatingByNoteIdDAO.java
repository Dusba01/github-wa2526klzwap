package dao.rating;

import dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Computes the average rating of a note.
 * Output: the average value, or {@code 0.0} if the note has no ratings.
 */
public final class GetAverageRatingByNoteIdDAO extends AbstractDAO<Double> {

    private static final String SQL =
            "SELECT AVG(value) AS avg_rating FROM rating WHERE note_id = ?";

    private final int noteId;

    public GetAverageRatingByNoteIdDAO(final Connection con, final int noteId) {
        super(con);
        this.noteId = noteId;
    }

    @Override
    protected void doAccess() throws SQLException {
        double average = 0.0;
        try (PreparedStatement ps = con.prepareStatement(SQL)) {
            ps.setInt(1, noteId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    average = rs.getDouble("avg_rating");
                }
            }
        }
        outputParam = average;
    }
}
