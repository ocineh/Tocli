package cli;

import models.Task;
import models.TodoList;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.util.Comparator;
import java.util.Date;

@Command(name = "list", description = "List all tasks", mixinStandardHelpOptions = true)
public class List extends SubCommand {
    @Option(
            names = {"--undone", "-u"},
            description = "List only undone tasks"
    ) private boolean undoneOnly;
    @Option(
            names = {"--done", "-d"},
            description = "List only done tasks"
    ) private boolean doneOnly;
    @Option(
            names = {"--title", "-t"},
            description = "List only tasks whose title matches the given regex",
            paramLabel = "<REGEX>"
    ) private String titleRegex;
    @Option(
            names = {"--added-before", "-B"},
            description = "List only tasks added before the given date (format: yyyy-MM-dd)",
            paramLabel = "<DATE>"
    ) private Date addedBefore;
    @Option(
            names = {"--added-after", "-A"},
            description = "List only tasks added after the given date (format: yyyy-MM-dd)",
            paramLabel = "<DATE>"
    ) private Date addedAfter;
    @Option(
            names = {"--sort", "-s"},
            paramLabel = "<SORT TYPE>",
            description = "Sort tasks by the given sort type (valid values: ${COMPLETION-CANDIDATES})"
    ) private SortType sortType;
    @Option(
            names = {"--reverse", "-r"},
            description = "Reverse the order of the tasks"
    ) private boolean reverse;

    @Override
    public void run() {
        System.out.println(getTodoListName() + ":");
        TodoList todoList = getData().get(getTodoListName());
        if(sortType != null) todoList.sort(sortType.getComparator(reverse));
        save();
        System.out.println(todoList.toString(task -> {
            return (!undoneOnly || !task.isDone()) && (!doneOnly || task.isDone()) &&
                    (titleRegex == null || task.getTitle().matches(titleRegex)) &&
                    (addedBefore == null || !task.getAdded().after(addedBefore)) &&
                    (addedAfter == null || !task.getAdded().before(addedAfter));
        }));
    }

    public enum SortType {
        TITLE, ADDED, DONE;

        public Comparator<Task> getComparator(boolean reverse) {
            switch(this) {
                case TITLE -> {
                    if(reverse) return (t1, t2) -> t2.getTitle().compareToIgnoreCase(t1.getTitle());
                    return (t1, t2) -> t1.getTitle().compareToIgnoreCase(t2.getTitle());
                }
                case ADDED -> {
                    if(reverse) return (t1, t2) -> t2.getAdded().compareTo(t1.getAdded());
                    return (t1, t2) -> t1.getAdded().compareTo(t2.getAdded());
                }
                case DONE -> {
                    if(reverse)
                        return (t1, t2) -> {
                            if(t1.isDone() == t2.isDone()) return 0;
                            return t1.isDone() ? 1 : -1;
                        };
                    return (t1, t2) -> {
                        if(t1.isDone() == t2.isDone()) return 0;
                        return t1.isDone() ? -1 : 1;
                    };
                }
            }
            return null;
        }
    }
}
