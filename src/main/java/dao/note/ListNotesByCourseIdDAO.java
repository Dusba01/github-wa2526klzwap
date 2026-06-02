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
 * Lists the notes belonging to a given course.
 * Output list: the {@link Note}s, most recent first.
 */
public final class ListNotesByCourseIdDAO extends AbstractDAO<Note> {

    private static final String SQL =
            "SELECT id, author_id, course_id, title, description, upload_date, file_path "
                    + "FROM note WHERE course_id = ? ORDER BY upload_date DESC";

    private final int courseId;

    public ListNotesByCourseIdDAO(final Connection con, final int courseId) {
        super(con);
        this.courseId = courseId;
    }

    @Override
    protected void doAccess() throws SQLException {
        final List<Note> notes = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(SQL)) {
            ps.setInt(1, courseId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    notes.add(NoteMapper.mapNote(rs));
                }
            }
        }
        outputListParam = notes;
    }
}
