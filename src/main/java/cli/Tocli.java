package cli;

import models.Data;
import picocli.CommandLine;
import picocli.CommandLine.*;
import picocli.CommandLine.Model.CommandSpec;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.Callable;

@Command(
        name = "tocli",
        mixinStandardHelpOptions = true,
        version = "tocli 0.1.5",
        subcommands = {
                List.class,
                Add.class,
                Rename.class,
                Delete.class,
                Export.class,
                Import.class,
                Undone.class,
                Done.class
        }
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
