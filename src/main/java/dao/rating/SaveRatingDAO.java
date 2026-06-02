package dao.rating;

import dao.AbstractDAO;
import model.Rating;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Inserts a rating or updates it if the user already rated the note.
 * Output: the stored {@link Rating}.
 */
public final class SaveRatingDAO extends AbstractDAO<Rating> {

    private static final String SQL =
            "INSERT INTO rating (user_id, note_id, value) VALUES (?, ?, ?) "
                    + "ON CONFLICT (user_id, note_id) DO UPDATE SET value = EXCLUDED.value";

    private final Rating rating;

    public SaveRatingDAO(final Connection con, final Rating rating) {
        super(con);
        this.rating = rating;
    }

    @Override
    protected void doAccess() throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(SQL)) {
            ps.setInt(1, rating.getUserId());
            ps.setInt(2, rating.getNoteId());
            ps.setInt(3, rating.getValue());
            ps.executeUpdate();
        }
        outputParam = rating;
    }
}
