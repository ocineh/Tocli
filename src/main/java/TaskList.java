import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.LinkedList;

public class TaskList implements Iterable<Task>, Serialize {
    private LinkedList<Task> tasks;

    public TaskList() {
        tasks = new LinkedList<>();
    }

    public void add(Task task) {
        tasks.add(task);
    }

    public void add(String title) {
        tasks.add(new Task(tasks.size(), title));
    }

    public void remove(Task task) {
        tasks.remove(task);
    }

    public boolean remove(int id) {
        return tasks.removeIf(task -> task.getId() == id);
    }

    public Task get(int id) {
        for(Task task: tasks) if(task.getId() == id) return task;
        return null;
    }

    public void load(Path path) {
        try {
            String content = Files.readString(path);
            tasks = Serialize.fromBase64(content);
        } catch(IOException | ClassNotFoundException ignored) {}
    }

    public void save(Path path) {
        try {
            Files.writeString(path, Serialize.toBase64(tasks));
        } catch(IOException ignored) {}
    }

    @Override
    public Iterator<Task> iterator() {
        if(tasks == null) return null;
        return tasks.iterator();
    }
}
