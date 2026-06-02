package dao.note;

import dao.AbstractDAO;
import model.Note;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Reads a note by id, restricted to a given author.
 * Output: the {@link Note}, or {@code null} if absent / not owned.
 */
public final class GetNoteByIdAndAuthorIdDAO extends AbstractDAO<Note> {

    private static final String SQL =
            "SELECT id, author_id, course_id, title, description, upload_date, file_path "
                    + "FROM note WHERE id = ? AND author_id = ?";

    private final int id;
    private final int authorId;

    public GetNoteByIdAndAuthorIdDAO(final Connection con, final int id, final int authorId) {
        super(con);
        this.id = id;
        this.authorId = authorId;
    }

    @Override
    protected void doAccess() throws SQLException {
        Note note = null;
        try (PreparedStatement ps = con.prepareStatement(SQL)) {
            ps.setInt(1, id);
            ps.setInt(2, authorId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    note = NoteMapper.mapNote(rs);
                }
            }
        }
        outputParam = note;
    }
}
