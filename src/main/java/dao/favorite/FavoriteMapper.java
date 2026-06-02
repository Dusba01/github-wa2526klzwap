package dao.favorite;

import model.Favorite;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/** Package-private helper mapping a {@link ResultSet} row to a {@link Favorite}. */
final class FavoriteMapper {

    private FavoriteMapper() {
    }

    static Favorite map(final ResultSet rs) throws SQLException {
        final Favorite favorite = new Favorite();
        favorite.setId(rs.getInt("id"));
        favorite.setUserId(rs.getInt("user_id"));
        favorite.setNoteId(rs.getInt("note_id"));

        final Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            favorite.setCreatedAt(createdAt.toLocalDateTime());
        }
        return favorite;
    }
}
