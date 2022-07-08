package cli;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.ParentCommand;

import java.util.Date;


@Command(name = "add", description = "Add a new task", mixinStandardHelpOptions = true)
public class Add implements Runnable {
    @Parameters(
            paramLabel = "<TITLE>",
            description = "The title of the task"
    ) private String title;
    @ParentCommand private Tocli tocli;
    @Option(
            names = {"-d", "--due"},
            paramLabel = "<DATE>",
            description = "The due date of the task"
    ) private Date dueDate;

    public void run() {
        tocli.getData().get(tocli.getTodoListName()).add(title, dueDate);
        System.out.println("Added task: " + title);
        tocli.save();
    }
}
