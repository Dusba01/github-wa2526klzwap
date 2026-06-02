package dao.rating;

import dao.AbstractDAO;
import model.Rating;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Lists every rating given by a user. Output list: the {@link Rating}s.
 */
public final class ListRatingsByUserIdDAO extends AbstractDAO<Rating> {

    private static final String SQL =
            "SELECT id, user_id, note_id, value, created_at FROM rating "
                    + "WHERE user_id = ? ORDER BY created_at DESC";

    private final int userId;

    public ListRatingsByUserIdDAO(final Connection con, final int userId) {
        super(con);
        this.userId = userId;
    }

    @Override
    protected void doAccess() throws SQLException {
        final List<Rating> ratings = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(SQL)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ratings.add(RatingMapper.map(rs));
                }
            }
        }
        outputListParam = ratings;
    }
}
