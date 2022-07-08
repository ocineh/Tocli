package cli;

import models.Task;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.util.Date;

@Command(name = "edit", description = "Edit a task.", mixinStandardHelpOptions = true)
public class Edit extends SubCommand {
    @Parameters(
            paramLabel = "<ID>",
            description = "The ID of the task."
    ) private int id;
    @Option(
            names = {"-t", "--title"},
            paramLabel = "<TITLE>",
            description = "The new title of the task."
    ) private String title;
    @Option(
            names = {"-d", "--due"},
            paramLabel = "<DATE>",
            description = "The new due date of the task."
    ) private Date dueDate;
    @Option(
            names = {"-D", "--done"},
            description = "Mark the task as done."
    ) private boolean done;
    @Option(
            names = {"-u", "--undone"},
            description = "Mark the task as undone."
    ) private boolean undone;

    public Edit() {}

    public Edit(Tocli tocli, int id, String title, Date dueDate, boolean done, boolean undone) {
        super(tocli);
        this.id = id;
        this.title = title;
        this.dueDate = dueDate;
        this.done = done;
        this.undone = undone;
    }

    @Override
    public void run() {
        Task task = getData().get(getTodoListName()).get(id);
        if(task == null) throwException("Task with ID " + id + " does not exist.");
        else {
            if(title != null && !title.isEmpty() && !title.isBlank()) task.setTitle(title);
            if(dueDate != null) task.setDueDate(dueDate);
            if(done) task.done();
            if(undone) task.undone();
            System.out.println("Edited task: " + task);
            save();
        }
    }
}
