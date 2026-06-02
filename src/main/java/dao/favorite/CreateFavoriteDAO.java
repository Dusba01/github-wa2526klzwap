package dao.favorite;

import dao.AbstractDAO;
import model.Favorite;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Adds a note to a user's favorites (idempotent). Output: the {@link Favorite}.
 */
public final class CreateFavoriteDAO extends AbstractDAO<Favorite> {

    private static final String SQL =
            "INSERT INTO favorite (user_id, note_id) VALUES (?, ?) "
                    + "ON CONFLICT (user_id, note_id) DO NOTHING";

    private final Favorite favorite;

    public CreateFavoriteDAO(final Connection con, final Favorite favorite) {
        super(con);
        this.favorite = favorite;
    }

    @Override
    protected void doAccess() throws SQLException {
        try (PreparedStatement ps =
                     con.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, favorite.getUserId());
            ps.setInt(2, favorite.getNoteId());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    favorite.setId(keys.getInt(1));
                }
            }
        }
        outputParam = favorite;
    }
}
