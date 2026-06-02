package dao.note;

import dao.AbstractDAO;
import model.Note;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Inserts a new note and fills in its generated id.
 * Output: the stored {@link Note} (with id populated).
 */
public final class CreateNoteDAO extends AbstractDAO<Note> {

    private static final String SQL =
            "INSERT INTO note (author_id, course_id, title, description, file_path) "
                    + "VALUES (?, ?, ?, ?, ?)";

    private final Note note;

    public CreateNoteDAO(final Connection con, final Note note) {
        super(con);
        this.note = note;
    }

    @Override
    protected void doAccess() throws SQLException {
        try (PreparedStatement ps =
                     con.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, note.getAuthorId());
            ps.setInt(2, note.getCourseId());
            ps.setString(3, note.getTitle());
            ps.setString(4, note.getDescription());
            ps.setString(5, note.getFilePath());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    note.setId(keys.getInt(1));
                }
            }
        }
        outputParam = note;
    }
}
