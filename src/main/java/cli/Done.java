package cli;

import models.Task;
import picocli.CommandLine.*;

@Command(name = "done", description = "Mark a task as done", mixinStandardHelpOptions = true)
public class Done implements Runnable {
    @Spec private Model.CommandSpec spec;
    @ParentCommand private Tocli tocli;
    @Parameters(paramLabel = "<ID>", description = "The ID of the task") private int id;

    @Override
    public void run() {
        Task task = tocli.getData().get(tocli.getTodoListName()).get(id);
        if(task != null) task.done();
        else throw new ParameterException(spec.commandLine(), "No task exists with this id.");
        tocli.save();
    }
}
