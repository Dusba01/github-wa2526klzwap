package model;

import java.time.LocalDateTime;

public class Note {

    private int id;
    private int authorId;
    private String authorUsername;
    private int courseId;
    private String courseName;
    private String title;
    private String description;
    private LocalDateTime uploadDate;
    private String filePath;

    public Note() {}

    public Note(int authorId, int courseId, String title,
                String description, String filePath) {
        this.authorId = authorId;
        this.courseId = courseId;
        this.title = title;
        this.description = description;
        this.filePath = filePath;
    }

    public Note(int id, int authorId, int courseId, String title,
                String description, LocalDateTime uploadDate, String filePath) {
        this.id = id;
        this.authorId = authorId;
        this.courseId = courseId;
        this.title = title;
        this.description = description;
        this.uploadDate = uploadDate;
        this.filePath = filePath;
        //this.downloadCount = downloadCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public String getAuthorUsername() { return authorUsername; }

    public void setAuthorUsername(String authorUsername) {this.authorUsername = authorUsername;}

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() { return courseName; }

    public void setCourseName(String courseName) { this.courseName = courseName; }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(LocalDateTime uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
