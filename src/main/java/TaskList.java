import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.function.Predicate;

public class TaskList implements Iterable<Task>, Serialize {
    private HashMap<String, LinkedList<Task>> tasks;

    public TaskList() {
        tasks = new HashMap<>();
    }

    public void add(String taskListName, Task task) {
        if(!tasks.containsKey(taskListName)) tasks.put(taskListName, new LinkedList<>());
        tasks.get(taskListName).add(task);
    }

    public void add(String taskListName, String title) {
        add(taskListName, new Task(title));
    }

    public boolean remove(String taskListName, int id) {
        try {
            tasks.get(taskListName).remove(id);
            return true;
        } catch(IndexOutOfBoundsException e) {
            return false;
        }
    }

    public Task get(String taskListName, int id) {
        try {
            return tasks.get(taskListName).get(id);
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
        return new It();
    }

    public String toString(Predicate<Task> taskPredicate) {
        if(tasks.isEmpty()) return "No task list.";

        int totalSize = tasks.values().stream().mapToInt(LinkedList::size).sum();
        int lengthMax = (int) (Math.log10(totalSize) + 1);
        String format = "%" + lengthMax + "d";

        StringBuilder sb = new StringBuilder();
        for(String taskListName: tasks.keySet()) {
            LinkedList<Task> taskList = tasks.get(taskListName);
            sb.append(taskListName).append(":\n");

            int i = 0;
            for(Task task: taskList) {
                if(!taskPredicate.test(task)) continue;

                sb.append(String.format(format, i))
                  .append(": ")
                  .append((task.isDone() ? "[X] " : "[ ] "))
                  .append(task.getTitle())
                  .append("\n");
                ++i;
            }
            if(i == 0) sb.append("No tasks\n");
            sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return toString(t -> true);
    }

    public int size() {
        return tasks.size();
    }

    public void addAll(TaskList other) {
        for(String taskListName: other.tasks.keySet()) {
            for(Task task: other.tasks.get(taskListName)) {
                if(task.getTitle() == null || task.getTitle().isEmpty()) continue;
                add(taskListName, new Task(
                        task.getTitle(),
                        task.getAdded() == null ? new Date() : task.getAdded(),
                        task.isDone()
                ));
            }
        }
    }

    private class It implements Iterator<Task> {
        private final Iterator<String> taskListNameIterator;
        private Iterator<Task> taskIterator;

        public It() {
            taskListNameIterator = tasks.keySet().iterator();
            if(taskListNameIterator.hasNext())
                taskIterator = tasks.get(taskListNameIterator.next()).iterator();
            else taskIterator = null;
        }

        @Override
        public boolean hasNext() {
            return taskIterator != null && taskIterator.hasNext() || taskListNameIterator.hasNext();
        }

        @Override
        public Task next() {
            if(taskIterator == null || !taskIterator.hasNext()) taskIterator = tasks.get(
                    taskListNameIterator.next()).iterator();
            return taskIterator.next();
        }
    }
}
