package dao.note;

import dao.AbstractDAO;
import model.Note;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Updates the course, title and description of a note owned by a given author.
 * Output: {@code true} if a row was updated, {@code false} otherwise.
 */
public final class UpdateNoteByIdAndAuthorIdDAO extends AbstractDAO<Boolean> {

    private static final String SQL =
            "UPDATE note SET course_id = ?, title = ?, description = ? "
                    + "WHERE id = ? AND author_id = ?";

    private final Note note;

    public UpdateNoteByIdAndAuthorIdDAO(final Connection con, final Note note) {
        super(con);
        this.note = note;
    }

    @Override
    protected void doAccess() throws SQLException {
        int affected;
        try (PreparedStatement ps = con.prepareStatement(SQL)) {
            ps.setInt(1, note.getCourseId());
            ps.setString(2, note.getTitle());
            ps.setString(3, note.getDescription());
            ps.setInt(4, note.getId());
            ps.setInt(5, note.getAuthorId());
            affected = ps.executeUpdate();
        }
        outputParam = affected > 0;
    }
}
