package cli;

import com.google.gson.stream.JsonReader;
import models.Data;
import models.Serialize;
import picocli.CommandLine.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Path;

@Command(
        name = "import",
        description = "Import tasks from a file in JSON format",
        mixinStandardHelpOptions = true
)
public class Import implements Runnable {
    @Spec private Model.CommandSpec spec;
    @Parameters(
            paramLabel = "<INPUT>",
            description = "The path of the file from where to import the tasks"
    ) private Path path;
    @ParentCommand private Tocli tocli;

    @Override
    public void run() {
        try {
            JsonReader reader = new JsonReader(new FileReader(path.toFile()));
            tocli.getData().merge(Serialize.fromJson(reader, Data.class));
            tocli.save();
        } catch(FileNotFoundException e) {
            throw new ParameterException(spec.commandLine(), "The input file does not exist.");
        }
    }
}
