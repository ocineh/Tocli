package models;

import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.function.Predicate;

public class TodoList implements Iterable<Task>, Serialize {
    private final LinkedList<Task> tasks;

    public TodoList() {
        this.tasks = new LinkedList<>();
    }

    public void add(Task task) {
        tasks.add(task);
    }

    public void add(String title, Date dueDate) {
        add(new Task(title, dueDate));
    }

    public void add(String title) {
        add(new Task(title));
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

    @Override
    public Iterator<Task> iterator() {
        return tasks.iterator();
    }

    public String toString(Predicate<Task> taskPredicate) {
        int lengthMax = (int) (Math.log10(tasks.size()) + 1);
        String format = "%" + lengthMax + "d";

        StringBuilder sb = new StringBuilder();
        int i = 0;
        for(Task task: tasks) {
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
        return sb.toString();
    }

    @Override
    public String toString() {
        return toString(t -> true);
    }

    public int size() {
        return tasks.size();
    }

    public void addAll(TodoList other) {
        tasks.addAll(other.tasks);
    }

    public LinkedList<Task> getAll() {
        return new LinkedList<>(tasks);
    }

    public void sort(Comparator<Task> comparator) {
        tasks.sort(comparator);
    }
}
