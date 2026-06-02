package model;

/**
 * Read model that pairs a course with the number of notes (documents)
 * uploaded for it. Produced by the course-summary DAO and consumed by the
 * home page.
 */
public class CourseSummary {

    private final int id;
    private final String name;
    private final int documentCount;

    public CourseSummary(int id, String name, int documentCount) {
        this.id = id;
        this.name = name;
        this.documentCount = documentCount;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getDocumentCount() {
        return documentCount;
    }
}
