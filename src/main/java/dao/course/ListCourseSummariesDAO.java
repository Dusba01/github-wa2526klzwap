package dao.course;

import dao.AbstractDAO;
import model.CourseSummary;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Lists each course together with the number of notes uploaded for it.
 * Output list: the {@link CourseSummary}s, most documented first.
 */
public final class ListCourseSummariesDAO extends AbstractDAO<CourseSummary> {

    private static final String SQL =
            "SELECT c.id, c.name, COUNT(n.id) AS document_count "
                    + "FROM course c "
                    + "LEFT JOIN note n ON n.course_id = c.id "
                    + "GROUP BY c.id, c.name "
                    + "ORDER BY document_count DESC, c.name ASC";

    public ListCourseSummariesDAO(final Connection con) {
        super(con);
    }

    @Override
    protected void doAccess() throws SQLException {
        final List<CourseSummary> summaries = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(SQL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                summaries.add(new CourseSummary(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("document_count")));
            }
        }
        outputListParam = summaries;
    }
}
