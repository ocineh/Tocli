package cli;

import models.Task;
import picocli.CommandLine.*;

@Command(name = "done", description = "Mark a task as done", mixinStandardHelpOptions = true)
public class Done extends SubCommand {
    @Parameters(paramLabel = "<ID>", description = "The ID of the task") private int id;

    @Override
    public void run() {
        Task task = getData().get(getTodoListName()).get(id);
        if(task != null) task.done();
        else throwException("No task exists with this id.");
        save();
    }
}
