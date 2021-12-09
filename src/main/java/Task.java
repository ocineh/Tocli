import java.util.Date;
import java.util.List;

public class Task {
    private static List<Task> tasks;
    private static Integer count = 0;
    private final Integer id;
    private final String title;
    private final Date added;
    private boolean done;

    public Task(Integer id, String title, Date added, boolean done) {
        this.id = id;
        this.title = title;
        this.added = added;
        this.done = done;
        ++Task.count;
    }

    public Task(String title) {
        this(Task.count, title, new Date(), false);
    }

    public static Integer getCount() {
        return count;
    }

    public static List<Task> getTasks() {
        return tasks;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
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
}
