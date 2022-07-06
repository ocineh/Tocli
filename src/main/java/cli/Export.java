package cli;

import models.Serialize;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

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
public class Export extends SubCommand {
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
        else try {
            writer = new FileWriter(file.toFile());
        } catch(IOException ignored) {
            throwException("Could not open output file.");
            return; // unreachable, to suppress the warning
        }

        try {
            String json = Serialize.toJson(getData(), pretty);
            writer.write(json);
            writer.flush();
            writer.close();
        } catch(IOException e) {
            throwException(
                    "Error while writing into the output file."
            );
        }
    }
}
