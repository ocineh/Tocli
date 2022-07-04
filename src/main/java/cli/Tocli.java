package cli;

import com.google.gson.stream.JsonReader;
import models.Data;
import models.Serialize;
import models.Task;
import picocli.CommandLine;
import picocli.CommandLine.*;
import picocli.CommandLine.Model.CommandSpec;

import java.io.*;
import java.nio.file.Path;
import java.util.concurrent.Callable;

@Command(
        name = "tocli",
        mixinStandardHelpOptions = true,
        version = "tocli 0.1.4",
        subcommands = {List.class}
)
public class Tocli implements Callable<Integer> {
    @Option(
            names = {"--file", "-f"},
            description = "Path to the todo file (default: ${DEFAULT-VALUE})"
    ) private final Path dataFile = Path.of(".tasks");
    private final Data data = new Data();
    @Spec CommandSpec spec;
    @Parameters(
            paramLabel = "<TODO LIST NAME>",
            description = "The name of the todo list (default: ${DEFAULT-VALUE})",
            defaultValue = "default"
    ) private String todoListName;

    {
        data.load(dataFile);
    }

    public static void main(String[] args) {
        System.exit(new CommandLine(new Tocli()).execute(args));
    }

    public Data getData() {
        return data;
    }

    public String getTodoListName() {
        return todoListName;
    }

    @Command(name = "add", description = "Add a new task", mixinStandardHelpOptions = true)
    public void add(
            @Parameters(paramLabel = "<TITLE>", description = "The title of the task") String title
    ) {
        data.get(todoListName).add(title);
        System.out.println("Added task: " + title);
        save();
    }

    @Command(name = "rename", description = "Rename a task", mixinStandardHelpOptions = true)
    public void rename(
            @Parameters(paramLabel = "<ID>", description = "The ID of the task") int id,
            @Parameters(
                    paramLabel = "<TITLE>",
                    description = "The new title of the task"
            ) String title
    ) {
        Task task = data.get(todoListName).get(id);
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
        save();
    }

    @Command(name = "delete", description = "Delete a task", mixinStandardHelpOptions = true)
    public void delete(
            @Parameters(paramLabel = "<ID>", description = "The ID of the task") int id
    ) {
        if(!data.get(todoListName).remove(id))
            throw new ParameterException(spec.commandLine(), "No task exists with this id.");
        save();
    }

    @Command(name = "done", description = "Mark a task as done", mixinStandardHelpOptions = true)
    public void done(@Parameters(paramLabel = "<ID>", description = "The ID of the task") int id)
    throws ParameterException {
        Task task = data.get(todoListName).get(id);
        if(task != null) task.done();
        else throw new ParameterException(spec.commandLine(), "No task exists with this id.");
        save();
    }

    @Command(
            name = "undone",
            description = "Mark a task as undone",
            mixinStandardHelpOptions = true
    )
    public void undone(@Parameters(paramLabel = "<ID>", description = "The ID of the task") int id)
    throws ParameterException {
        Task task = data.get(todoListName).get(id);
        if(task != null) task.undone();
        else throw new ParameterException(spec.commandLine(), "No task exists with this id.");
        save();
    }

    @Command(
            name = "export",
            description = "Export the tasks to a file (if specified) in JSON format",
            mixinStandardHelpOptions = true
    )
    public void exportTasks(
            @Option(
                    names = {"-o", "--output"},
                    description = "The path of the file where to esxport the tasks (default: in the terminal)"
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
            String json = Serialize.toJson(data, pretty);
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
            this.data.merge(Serialize.fromJson(reader, Data.class));
            save();
        } catch(FileNotFoundException e) {
            throw new ParameterException(spec.commandLine(), "The input file does not exist.");
        }
    }

    private void save() {
        try {
            data.save(dataFile);
        } catch(IOException e) {
            throw new ParameterException(
                    spec.commandLine(),
                    "Error while writing into the data file."
            );
        }
    }

    @Override
    public Integer call() {
        return new CommandLine(new Tocli()).execute("--help");
    }
}
