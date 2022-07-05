package cli;

import picocli.CommandLine;
import picocli.CommandLine.*;

@Command(name = "delete", description = "Delete a task", mixinStandardHelpOptions = true)
public class Delete implements Runnable {
    @Spec private Model.CommandSpec spec;
    @Parameters(paramLabel = "<ID>", description = "The ID of the task") private int id;
    @ParentCommand private Tocli tocli;

    @Override
    public void run() {
        if(!tocli.getData().get(tocli.getTodoListName()).remove(id))
            throw new CommandLine.ParameterException(
                    spec.commandLine(),
                    "No task exists with this id."
            );
        tocli.save();
    }
}
