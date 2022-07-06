package cli;

import models.Task;
import picocli.CommandLine.*;

@Command(name = "undone", description = "Mark a task as undone", mixinStandardHelpOptions = true)
public class Undone extends SubCommand {
    @Parameters(paramLabel = "<ID>", description = "The ID of the task") private int id;

    @Override
    public void run() {
        Task task = getData().get(getTodoListName()).get(id);
        if(task != null) task.undone();
        else throwException("No task exists with this id.");
        save();
    }
}
