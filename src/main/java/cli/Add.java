package cli;

import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.ParentCommand;


@Command(name = "add", description = "Add a new task", mixinStandardHelpOptions = true)
public class Add implements Runnable {
    @Parameters(
            paramLabel = "<TITLE>",
            description = "The title of the task"
    ) private String title;
    @ParentCommand private Tocli tocli;

    public void run() {
        tocli.getData().get(tocli.getTodoListName()).add(title);
        System.out.println("Added task: " + title);
        tocli.save();
    }
}
