package cli;

import models.Task;
import picocli.CommandLine.*;

@Command(name = "undone", description = "Mark a task as undone", mixinStandardHelpOptions = true)
public class Undone implements Runnable {
    @Spec private Model.CommandSpec spec;
    @ParentCommand private Tocli tocli;
    @Parameters(paramLabel = "<ID>", description = "The ID of the task") private int id;

    @Override
    public void run() {
        Task task = tocli.getData().get(tocli.getTodoListName()).get(id);
        if(task != null) task.undone();
        else throw new ParameterException(spec.commandLine(), "No task exists with this id.");
        tocli.save();
    }
}
