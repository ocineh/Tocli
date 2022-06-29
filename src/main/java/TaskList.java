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
        tasks.add(new Task(title));
    }

    public void remove(Task task) {
        tasks.remove(task);
    }

    public boolean remove(int id) {
        try {
            tasks.remove(id);
            return true;
        } catch(IndexOutOfBoundsException e) {
            return false;
        }
    }

    public Task get(int id) {
        try {
            return tasks.get(id);
        } catch(IndexOutOfBoundsException e) {
            return null;
        }
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

    @Override
    public String toString() {
        int lengthMax = (int) (Math.log10(tasks.size()) + 1);
        String format = "%" + lengthMax + "d";
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            sb.append(String.format(format, i))
                    .append(": ")
                    .append((task.isDone() ? "[X] " : "[ ] "))
                    .append(task.getTitle())
                    .append("\n");
        }
        return sb.toString();
    }

    public int size() {
        return tasks.size();
    }
}
