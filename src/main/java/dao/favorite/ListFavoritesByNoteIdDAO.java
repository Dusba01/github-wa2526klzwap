package dao.favorite;

import dao.AbstractDAO;
import model.Favorite;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Lists every favorite pointing at a given note. Output list: the {@link Favorite}s.
 */
public final class ListFavoritesByNoteIdDAO extends AbstractDAO<Favorite> {

    private static final String SQL =
            "SELECT id, user_id, note_id, created_at FROM favorite "
                    + "WHERE note_id = ? ORDER BY created_at DESC";

    private final int noteId;

    public ListFavoritesByNoteIdDAO(final Connection con, final int noteId) {
        super(con);
        this.noteId = noteId;
    }

    @Override
    protected void doAccess() throws SQLException {
        final List<Favorite> favorites = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(SQL)) {
            ps.setInt(1, noteId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    favorites.add(FavoriteMapper.map(rs));
                }
            }
        }
        outputListParam = favorites;
    }
}
