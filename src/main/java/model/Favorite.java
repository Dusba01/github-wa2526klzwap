package model;

import java.time.LocalDateTime;

public class Favorite {

    private int id;
    private int userId;
    private int noteId;
    private LocalDateTime createdAt;

    public Favorite() {}

    public Favorite(int userId, int noteId) {
        this.userId = userId;
        this.noteId = noteId;
    }

    public Favorite(int id, int userId, int noteId, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.noteId = noteId;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
