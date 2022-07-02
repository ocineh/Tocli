package cli;

import com.google.gson.stream.JsonReader;
import models.Serialize;
import models.Task;
import models.TaskList;
import picocli.CommandLine;
import picocli.CommandLine.*;
import picocli.CommandLine.Model.CommandSpec;

import java.io.*;
import java.nio.file.Path;
import java.util.Date;
import java.util.concurrent.Callable;

@Command(name = "tocli", mixinStandardHelpOptions = true, version = "tocli 0.1.3")
public class Todo implements Callable<Integer> {
    @Option(
            names = {"--file", "-f"},
            description = "Path to the todo file (default: ${DEFAULT-VALUE})"
    ) private final Path data = Path.of(".tasks");
    @Spec CommandSpec spec;
    @Parameters(
            paramLabel = "<TASK LIST NAME>",
            description = "The name of the task list (default: ${DEFAULT-VALUE})",
            defaultValue = "default"
    ) private String taskListName;

    {
        TaskList.getInstance().load(data);
    }

    public static void main(String[] args) {
        System.exit(new CommandLine(new Todo()).execute(args));
    }

    @Command(name = "list", description = "List all tasks", mixinStandardHelpOptions = true)
    public void list(
            @Option(names = "--undone", description = "List only undone tasks") boolean undoneOnly,
            @Option(names = "--done", description = "List only done tasks") boolean doneOnly,
            @Option(
                    names = "--title",
                    description = "List only tasks whose title matches the given regex",
                    paramLabel = "<REGEX>"
            ) String titleRegex,
            @Option(
                    names = "--added-before",
                    description = "List only tasks added before the given date (format: yyyy-MM-dd)",
                    paramLabel = "<DATE>"
            ) Date addedBefore,
            @Option(
                    names = "--added-after",
                    description = "List only tasks added after the given date (format: yyyy-MM-dd)",
                    paramLabel = "<DATE>"
            ) Date addedAfter
    ) {
        System.out.println(TaskList.getInstance().toString(task -> {
            return (!undoneOnly || !task.isDone()) && (!doneOnly || task.isDone()) &&
                    (titleRegex == null || task.getTitle().matches(titleRegex)) &&
                    (addedBefore == null || !task.getAdded().after(addedBefore)) &&
                    (addedAfter == null || !task.getAdded().before(addedAfter));
        }));
    }

    @Command(name = "add", description = "Add a new task", mixinStandardHelpOptions = true)
    public void add(
            @Parameters(paramLabel = "<TITLE>", description = "The title of the task") String title
    ) {
        TaskList.getInstance().add(taskListName, title);
        TaskList.getInstance().save(data);
    }

    @Command(name = "rename", description = "Rename a task", mixinStandardHelpOptions = true)
    public void rename(
            @Parameters(paramLabel = "<ID>", description = "The ID of the task") int id,
            @Parameters(
                    paramLabel = "<TITLE>",
                    description = "The new title of the task"
            ) String title
    ) {
        Task task = TaskList.getInstance().get(taskListName, id);
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
        TaskList.getInstance().save(data);
    }

    @Command(name = "delete", description = "Delete a task", mixinStandardHelpOptions = true)
    public void delete(
            @Parameters(paramLabel = "<ID>", description = "The ID of the task") int id
    ) {
        if(!TaskList.getInstance().remove(taskListName, id))
            throw new ParameterException(spec.commandLine(), "No task exists with this id.");
        TaskList.getInstance().save(data);
    }

    @Command(name = "done", description = "Mark a task as done", mixinStandardHelpOptions = true)
    public void done(@Parameters(paramLabel = "<ID>", description = "The ID of the task") int id)
    throws ParameterException {
        Task task = TaskList.getInstance().get(taskListName, id);
        if(task != null) task.done();
        else throw new ParameterException(spec.commandLine(), "No task exists with this id.");
    }

    @Command(
            name = "undone",
            description = "Mark a task as undone",
            mixinStandardHelpOptions = true
    )
    public void undone(@Parameters(paramLabel = "<ID>", description = "The ID of the task") int id)
    throws ParameterException {
        Task task = TaskList.getInstance().get(taskListName, id);
        if(task != null) task.undone();
        else throw new ParameterException(spec.commandLine(), "No task exists with this id.");
    }

    @Command(
            name = "export",
            description = "Export the tasks to a file (if specified) in JSON format",
            mixinStandardHelpOptions = true
    )
    public void exportTasks(
            @Option(
                    names = {"-o", "--output"},
                    description = "The path of the file where to export the tasks (default: in the terminal)"
            ) Path file,
            @Option(
                    names = {"--pretty", "-p"},
                    description = "Pretty print the JSON file (default: ${DEFAULT-VALUE})"
            ) boolean pretty
    ) {
        Writer writer;
        if(file == null) writer = new OutputStreamWriter(System.out);
        else {
            try {
                writer = new FileWriter(file.toFile());
            } catch(IOException ignored) {
                throw new ParameterException(spec.commandLine(), "Could not open output file.");
            }
        }

        try {
            String json = Serialize.toJson(TaskList.getInstance(), pretty);
            writer.write(json);
            writer.flush();
            writer.close();
        } catch(IOException e) {
            throw new ParameterException(
                    spec.commandLine(),
                    "Error while writing into the output file."
            );
        }
    }

    @Command(
            name = "import",
            description = "Import tasks from a file in JSON format",
            mixinStandardHelpOptions = true
    )
    public void importTasks(
            @Parameters(
                    paramLabel = "<INPUT>",
                    description = "The path of the file from where to import the tasks"
            ) Path path
    ) {
        try {
            JsonReader reader = new JsonReader(new FileReader(path.toFile()));
            TaskList.getInstance().addAll(Serialize.fromJson(reader, TaskList.class));
        } catch(FileNotFoundException e) {
            throw new ParameterException(spec.commandLine(), "The input file does not exist.");
        } finally {
            TaskList.getInstance().save(data);
        }
    }

    @Override
    public Integer call() {
        list(false, false, null, null, null);
        return 0;
    }
}
