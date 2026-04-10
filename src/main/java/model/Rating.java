package model;

import java.time.LocalDateTime;

public class Rating {

    private int id;
    private int userId;
    private int noteId;
    private int value;
    private LocalDateTime createdAt;

    public Rating() {}

    public Rating(int userId, int noteId, int value) {
        this.userId = userId;
        this.noteId = noteId;
        this.value = value;
    }

    public Rating(int id, int userId, int noteId, int value, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.noteId = noteId;
        this.value = value;
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

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
