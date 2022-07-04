package cli;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParentCommand;

import java.util.Date;

@Command(name = "list", description = "List all tasks", mixinStandardHelpOptions = true)
public class List implements Runnable {
    @Option(
            names = "--undone",
            description = "List only undone tasks"
    ) boolean undoneOnly;
    @Option(names = "--done", description = "List only done tasks") boolean doneOnly;
    @Option(
            names = "--title",
            description = "List only tasks whose title matches the given regex",
            paramLabel = "<REGEX>"
    ) String titleRegex;
    @Option(
            names = "--added-before",
            description = "List only tasks added before the given date (format: yyyy-MM-dd)",
            paramLabel = "<DATE>"
    ) Date addedBefore;
    @Option(
            names = "--added-after",
            description = "List only tasks added after the given date (format: yyyy-MM-dd)",
            paramLabel = "<DATE>"
    ) Date addedAfter;
    @ParentCommand private Tocli tocli;

    @Override
    public void run() {
        System.out.println(tocli.getTodoListName() + ":");
        System.out.println(tocli.getData().get(tocli.getTodoListName()).toString(
                task -> (!undoneOnly || !task.isDone()) && (!doneOnly || task.isDone()) &&
                        (titleRegex == null || task.getTitle().matches(titleRegex)) &&
                        (addedBefore == null || !task.getAdded().after(addedBefore)) &&
                        (addedAfter == null || !task.getAdded().before(addedAfter))
        ));
    }
}
