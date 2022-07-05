package cli;

import models.Task;
import picocli.CommandLine.*;

@Command(name = "rename", description = "Rename a task", mixinStandardHelpOptions = true)
public class Rename implements Runnable {
    @Spec Model.CommandSpec spec;
    @Parameters(paramLabel = "<ID>", description = "The ID of the task") private int id;
    @Parameters(
            paramLabel = "<TITLE>",
            description = "The new title of the task"
    ) private String title;
    @ParentCommand private Tocli tocli;

    public void run() {
        Task task = tocli.getData().get(tocli.getTodoListName()).get(id);
        if(task == null) throw new ParameterException(
                spec.commandLine(),
                "No task exists with this id."
        );
        if(title == null || title.isEmpty() || title.isBlank())
            throw new ParameterException(
                    spec.commandLine(),
                    "The new task title cannot be empty or contain only spaces."
            );
        task.setTitle(title);
        tocli.save();
    }
}
