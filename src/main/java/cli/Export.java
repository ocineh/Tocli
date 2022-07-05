package cli;

import models.Serialize;
import picocli.CommandLine;
import picocli.CommandLine.*;

import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Path;

@Command(
        name = "export",
        description = "Export the tasks to a file (if specified) in JSON format",
        mixinStandardHelpOptions = true
)
public class Export implements Runnable {
    @Spec Model.CommandSpec spec;
    @ParentCommand private Tocli tocli;
    @Option(
            names = {"-o", "--output"},
            description = "The path of the file where to esxport the tasks (default: in the terminal)"
    ) private Path file;
    @Option(
            names = {"--pretty", "-p"},
            description = "Pretty print the JSON file (default: ${DEFAULT-VALUE})"
    ) private boolean pretty;

    @Override
    public void run() {
        Writer writer;
        if(file == null) writer = new OutputStreamWriter(System.out);
        else {
            try {
                writer = new FileWriter(file.toFile());
            } catch(IOException ignored) {
                throw new CommandLine.ParameterException(
                        spec.commandLine(),
                        "Could not open output file."
                );
            }
        }

        try {
            String json = Serialize.toJson(tocli.getData(), pretty);
            writer.write(json);
            writer.flush();
            writer.close();
        } catch(IOException e) {
            throw new CommandLine.ParameterException(
                    spec.commandLine(),
                    "Error while writing into the output file."
            );
        }
    }
}
