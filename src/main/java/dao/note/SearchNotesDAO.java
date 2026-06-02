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
 * Full-text-ish search over notes by title, description, course or author.
 * Output list: the matching {@link Note}s, most recent first.
 */
public final class SearchNotesDAO extends AbstractDAO<Note> {

    private static final String SQL =
            "SELECT n.id, n.title, n.description, n.upload_date, n.file_path, "
                    + "       n.course_id, n.author_id, "
                    + "       c.name AS course_name, u.username AS author_username "
                    + "FROM note n "
                    + "JOIN course c ON n.course_id = c.id "
                    + "JOIN users u ON n.author_id = u.id "
                    + "WHERE LOWER(n.title) LIKE ? OR LOWER(n.description) LIKE ? "
                    + "OR LOWER(c.name) LIKE ? OR LOWER(u.username) LIKE ? "
                    + "ORDER BY n.upload_date DESC";

    private final String query;

    public SearchNotesDAO(final Connection con, final String query) {
        super(con);
        this.query = query;
    }

    @Override
    protected void doAccess() throws SQLException {
        final List<Note> notes = new ArrayList<>();
        final String like = "%" + query.toLowerCase() + "%";
        try (PreparedStatement ps = con.prepareStatement(SQL)) {
            ps.setString(1, like);
            ps.setString(2, like);
            ps.setString(3, like);
            ps.setString(4, like);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    notes.add(NoteMapper.mapNoteWithDetails(rs));
                }
            }
        }
        outputListParam = notes;
    }
}
