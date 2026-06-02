package dao.note;

import dao.AbstractDAO;
import model.Note;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Reads a note by its id. Output: the {@link Note}, or {@code null} if absent.
 */
public final class GetNoteByIdDAO extends AbstractDAO<Note> {

    private static final String SQL =
            "SELECT id, author_id, course_id, title, description, upload_date, file_path "
                    + "FROM note WHERE id = ?";

    private final int id;

    public GetNoteByIdDAO(final Connection con, final int id) {
        super(con);
        this.id = id;
    }

    @Override
    protected void doAccess() throws SQLException {
        Note note = null;
        try (PreparedStatement ps = con.prepareStatement(SQL)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    note = NoteMapper.mapNote(rs);
                }
            }
        }
        outputParam = note;
    }
}
