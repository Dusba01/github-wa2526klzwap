package dao.note;

import dao.AbstractDAO;
import model.Note;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Lists all notes, most recent first. Output list: the {@link Note}s.
 */
public final class ListNotesDAO extends AbstractDAO<Note> {

    private static final String SQL =
            "SELECT id, author_id, course_id, title, description, upload_date, file_path "
                    + "FROM note ORDER BY upload_date DESC";

    public ListNotesDAO(final Connection con) {
        super(con);
    }

    @Override
    protected void doAccess() throws SQLException {
        final List<Note> notes = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(SQL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                notes.add(NoteMapper.mapNote(rs));
            }
        }
        outputListParam = notes;
    }
}
