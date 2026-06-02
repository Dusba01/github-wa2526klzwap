package dao.note;

import model.Note;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * Package-private helper that maps a {@link ResultSet} row to a {@link Note}.
 * Kept in one place so the note DAOs share a single, consistent mapping.
 */
final class NoteMapper {

    private NoteMapper() {
    }

    /** Maps the base note columns. */
    static Note mapNote(final ResultSet rs) throws SQLException {
        final Note note = new Note();
        note.setId(rs.getInt("id"));
        note.setAuthorId(rs.getInt("author_id"));
        note.setCourseId(rs.getInt("course_id"));
        note.setTitle(rs.getString("title"));
        note.setDescription(rs.getString("description"));
        note.setFilePath(rs.getString("file_path"));

        final Timestamp uploadDate = rs.getTimestamp("upload_date");
        if (uploadDate != null) {
            note.setUploadDate(uploadDate.toLocalDateTime());
        }
        return note;
    }

    /** Maps the base columns plus the joined course name and author username. */
    static Note mapNoteWithDetails(final ResultSet rs) throws SQLException {
        final Note note = mapNote(rs);
        note.setCourseName(rs.getString("course_name"));
        note.setAuthorUsername(rs.getString("author_username"));
        return note;
    }
}
