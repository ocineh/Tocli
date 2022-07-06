package cli;

import models.Task;
import picocli.CommandLine.Command;
import picocli.CommandLine.ParameterException;
import picocli.CommandLine.Parameters;

@Command(name = "rename", description = "Rename a task", mixinStandardHelpOptions = true)
public class Rename extends SubCommand {
    @Parameters(paramLabel = "<ID>", description = "The ID of the task") private int id;
    @Parameters(
            paramLabel = "<TITLE>",
            description = "The new title of the task"
    ) private String title;

    public void run() {
        Task task = getData().get(getTodoListName()).get(id);
        if(task == null) throw new ParameterException(
                getSpec().commandLine(),
                "No task exists with this id."
        );
        if(title == null || title.isEmpty() || title.isBlank())
            throwException("The new task title cannot be empty or contain only spaces.");
        task.setTitle(title);
        save();
    }
}
