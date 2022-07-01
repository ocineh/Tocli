import java.util.Date;

public class Task implements Serialize {
    private final Date added;
    private String title;
    private boolean done;

    public Task(String title, Date added, boolean done) {
        this.title = title;
        this.added = added == null ? new Date() : added;
        this.done = done;
    }

    public Task(String title) {
        this(title, new Date(), false);
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
