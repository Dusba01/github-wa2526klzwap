package dao.note;

import dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Deletes a note by id, restricted to a given author.
 * Output: {@code true} if a row was deleted, {@code false} otherwise.
 */
public final class DeleteNoteByIdAndAuthorIdDAO extends AbstractDAO<Boolean> {

    private static final String SQL =
            "DELETE FROM note WHERE id = ? AND author_id = ?";

    private final int id;
    private final int authorId;

    public DeleteNoteByIdAndAuthorIdDAO(final Connection con, final int id, final int authorId) {
        super(con);
        this.id = id;
        this.authorId = authorId;
    }

    @Override
    protected void doAccess() throws SQLException {
        int affected;
        try (PreparedStatement ps = con.prepareStatement(SQL)) {
            ps.setInt(1, id);
            ps.setInt(2, authorId);
            affected = ps.executeUpdate();
        }
        outputParam = affected > 0;
    }
}
