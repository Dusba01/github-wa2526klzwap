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
 * Lists the notes uploaded by a given author, with course and author details.
 * Output list: the {@link Note}s, most recent first.
 */
public final class ListNotesByAuthorIdDAO extends AbstractDAO<Note> {

    private static final String SQL =
            "SELECT n.id, n.author_id, n.course_id, n.title, n.description, n.upload_date, n.file_path, "
                    + "       c.name AS course_name, u.username AS author_username "
                    + "FROM note n "
                    + "JOIN course c ON n.course_id = c.id "
                    + "JOIN users u ON n.author_id = u.id "
                    + "WHERE n.author_id = ? "
                    + "ORDER BY n.upload_date DESC";

    private final int authorId;

    public ListNotesByAuthorIdDAO(final Connection con, final int authorId) {
        super(con);
        this.authorId = authorId;
    }

    @Override
    protected void doAccess() throws SQLException {
        final List<Note> notes = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(SQL)) {
            ps.setInt(1, authorId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    notes.add(NoteMapper.mapNoteWithDetails(rs));
                }
            }
        }
        outputListParam = notes;
    }
}
