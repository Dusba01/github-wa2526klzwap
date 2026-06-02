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
 * Lists the notes a given user has marked as favorite.
 * Output list: the {@link Note}s, most recently favorited first.
 */
public final class ListFavoriteNotesByUserIdDAO extends AbstractDAO<Note> {

    private static final String SQL =
            "SELECT n.id, n.title, n.description, n.upload_date, n.file_path, "
                    + "       n.course_id, n.author_id, "
                    + "       c.name AS course_name, u.username AS author_username "
                    + "FROM favorite f "
                    + "JOIN note n ON f.note_id = n.id "
                    + "JOIN course c ON n.course_id = c.id "
                    + "JOIN users u ON n.author_id = u.id "
                    + "WHERE f.user_id = ? "
                    + "ORDER BY f.created_at DESC";

    private final int userId;

    public ListFavoriteNotesByUserIdDAO(final Connection con, final int userId) {
        super(con);
        this.userId = userId;
    }

    @Override
    protected void doAccess() throws SQLException {
        final List<Note> notes = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(SQL)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    notes.add(NoteMapper.mapNoteWithDetails(rs));
                }
            }
        }
        outputListParam = notes;
    }
}
