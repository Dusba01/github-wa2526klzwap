package dao.rating;

import dao.AbstractDAO;
import model.Rating;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Reads a user's rating of a note.
 * Output: the {@link Rating}, or {@code null} if absent.
 */
public final class GetRatingByUserAndNoteDAO extends AbstractDAO<Rating> {

    private static final String SQL =
            "SELECT id, user_id, note_id, value, created_at FROM rating "
                    + "WHERE user_id = ? AND note_id = ?";

    private final int userId;
    private final int noteId;

    public GetRatingByUserAndNoteDAO(final Connection con, final int userId, final int noteId) {
        super(con);
        this.userId = userId;
        this.noteId = noteId;
    }

    @Override
    protected void doAccess() throws SQLException {
        Rating rating = null;
        try (PreparedStatement ps = con.prepareStatement(SQL)) {
            ps.setInt(1, userId);
            ps.setInt(2, noteId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    rating = RatingMapper.map(rs);
                }
            }
        }
        outputParam = rating;
    }
}
