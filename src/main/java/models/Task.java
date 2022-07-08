package models;

import java.util.Date;

public class Task implements Serialize {
    private final Date added;
    private Date dueDate;
    private String title;
    private boolean done;

    public Task(String title, Date added, Date dueDate, boolean done) {
        this.title = title;
        this.added = added == null ? new Date() : added;
        this.dueDate = dueDate;
        this.done = done;
    }

    public Task(String title, Date dueDate) {
        this(title, new Date(), dueDate, false);
    }

    public Task(String title) {
        this(title, null, null, false);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getAdded() {
        return added;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isDone() {
        return done;
    }

    public void done() {
        this.done = true;
    }

    public void undone() {
        this.done = false;
    }

    @Override
    public String toString() {
        return (done ? "[X] " : "[ ] ") + ": " + title;
    }
}
