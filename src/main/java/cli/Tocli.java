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
        version = "tocli 0.1.5",
        subcommands = {List.class, Add.class, Rename.class, Delete.class, Export.class}
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

    void save() {
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
