package dao.rating;

import model.Rating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/** Package-private helper mapping a {@link ResultSet} row to a {@link Rating}. */
final class RatingMapper {

    private RatingMapper() {
    }

    static Rating map(final ResultSet rs) throws SQLException {
        final Rating rating = new Rating();
        rating.setId(rs.getInt("id"));
        rating.setUserId(rs.getInt("user_id"));
        rating.setNoteId(rs.getInt("note_id"));
        rating.setValue(rs.getInt("value"));

        final Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            rating.setCreatedAt(createdAt.toLocalDateTime());
        }
        return rating;
    }
}
