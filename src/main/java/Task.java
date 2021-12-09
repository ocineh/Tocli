import com.opencsv.bean.CsvDate;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Task {
    private static List<Task> tasks;
    private static Integer count = 0;

    static {Task.Bean.load();}

    private final Integer id;
    private String title;
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

    public static void addTask(Task task){
        tasks.add(task);
    }

    public static boolean deleteTask(int id){
        return tasks.removeIf(task -> task.getId() == id);
    }

    public static Task getTaskById(int id){
        for(Task task: tasks) if(task.id == id) return task;
        return null;
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

    public static class Bean {
        private Integer id;
        private String title;
        @CsvDate(writeFormat = "dd-MM-yyyy hh:mm:ss")
        private Date added;
        private boolean done;

        public Bean() {}

        public Bean(Task task) {
            id = task.getId();
            title = task.getTitle();
            added = task.getAdded();
            done = task.isDone();
        }

        private static void load() {
            try {
                Reader reader = new FileReader(".tasks.csv");
                tasks = new CsvToBeanBuilder<Bean>(reader).withQuoteChar('"').withSeparator(';').withType(Task.Bean.class).build().parse().stream().map(Task.Bean::build).collect(Collectors.toList());
            } catch(Exception ignore) { tasks = new LinkedList<>(); }
        }

        public static void save() {
            try {
                Writer writer = new FileWriter(".tasks.csv");
                StatefulBeanToCsv<Bean> beanToCsv = new StatefulBeanToCsvBuilder<Bean>(writer).withQuotechar('"').withSeparator(';').build();
                beanToCsv.write(tasks.stream().map(Task.Bean::new).collect(Collectors.toList()));
                writer.close();
            } catch(Exception ignore) {}
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setAdded(Date added) {
            this.added = added;
        }

        public void setDone(boolean done) {
            this.done = done;
        }

        public Task build() {
            return new Task(id, title, added, done);
        }
    }
}
