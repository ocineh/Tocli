package cli;

import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "delete", description = "Delete a task", mixinStandardHelpOptions = true)
public class Delete extends SubCommand {
    @Parameters(paramLabel = "<ID>", description = "The ID of the task") private int id;

    @Override
    public void run() {
        if(!getData().get(getTodoListName()).remove(id))
            throwException("No task exists with this id.");
        save();
    }
}
