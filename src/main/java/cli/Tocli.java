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
        version = "tocli 0.2.0",
        subcommands = {
                List.class,
                Add.class,
                Delete.class,
                Export.class,
                Import.class,
                Edit.class,
        }
)
public class Tocli implements Callable<Integer> {
    @Option(
            names = {"--file", "-f"},
            description = "Path to the todo file (default: ${DEFAULT-VALUE})"
    ) private final Path dataFile = Path.of(".tasks");
    private final Data data = new Data();
    @Spec CommandSpec spec;
    private boolean loaded = false;
    @Parameters(
            paramLabel = "<TODO LIST NAME>",
            description = "The name of the todo list (default: ${DEFAULT-VALUE})",
            defaultValue = "default"
    ) private String todoListName;

    public static void main(String[] args) {
        System.exit(new CommandLine(new Tocli())
                            .setCaseInsensitiveEnumValuesAllowed(true)
                            .execute(args));
    }

    public Data getData() {
        if(!loaded) load();
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

    void load() {
        try {
            data.load(dataFile);
            loaded = true;
        } catch(IOException e) {
            throw new ParameterException(
                    spec.commandLine(),
                    "Error while reading from the data file."
            );
        }
    }

    @Command(
            name = "done",
            description = "Mark a task as done.",
            mixinStandardHelpOptions = true
    )
    public void done(
            @Parameters(
                    paramLabel = "<ID>",
                    description = "The ID of the task."
            ) int id
    ) {
        new Edit(this, id, null, null, true, false).run();
    }

    @Command(
            name = "undone",
            description = "Mark a task as undone.",
            mixinStandardHelpOptions = true
    )
    public void undone(
            @Parameters(
                    paramLabel = "<ID>",
                    description = "The ID of the task."
            ) int id
    ) {
        new Edit(this, id, null, null, false, true).run();
    }

    @Command(
            name = "rename",
            description = "Rename a task.",
            mixinStandardHelpOptions = true
    )
    public void rename(
            @Parameters(
                    paramLabel = "<ID>",
                    description = "The ID of the task."
            ) int id,
            @Parameters(
                    paramLabel = "<TITLE>",
                    description = "The new title of the task."
            ) String title
    ) {
        new Edit(this, id, title, null, false, false).run();
    }

    @Override
    public Integer call() {
        main(new String[]{"--help"});
        return 0;
    }

    public CommandSpec getSpec() {
        return spec;
    }
}
