import java.util.Date;

public class Task implements Serialize {

    private final Integer id;
    private final Date added;
    private String title;
    private boolean done;

    public Task(Integer id, String title, Date added, boolean done) {
        this.id = id;
        this.title = title;
        this.added = added;
        this.done = done;
    }

    public Task(Integer id, String title) {
        this(id, title, new Date(), false);
    }

    public Integer getId() {
        return id;
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
        return (done ? "[X] " : "[ ] ") + id + ": " + title;
    }
}
