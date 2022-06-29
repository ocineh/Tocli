import picocli.CommandLine;
import picocli.CommandLine.*;
import picocli.CommandLine.Model.CommandSpec;

import java.nio.file.Path;
import java.util.concurrent.Callable;

@Command(name = "tocli", mixinStandardHelpOptions = true, version = "tocli 0.1.1")
public class Todo implements Callable<Integer> {
    private final TaskList tasks = new TaskList();
    @Option(
            names = {"--file", "-f"},
            description = "Path to the todo file (default: ${DEFAULT-VALUE})"
    )
    private final Path data = Path.of(".tasks");
    @Spec CommandSpec spec;

    {
        tasks.load(data);
    }

    public static void main(String[] args) {
        System.exit(new CommandLine(new Todo()).execute(args));
    }

    @Command(name = "list")
    public void list() {
        System.out.println(tasks);
    }

    @Command(name = "add")
    public void add(@Parameters String title) {
        tasks.add(title);
        tasks.save(data);
    }

    @Command(name = "rename")
    public void rename(@Parameters int id, @Parameters String title) {
        Task task = tasks.get(id);
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
        tasks.save(data);
    }

    @Command(name = "delete")
    public void delete(@Parameters int id) {
        if(!tasks.remove(id)) throw new ParameterException(
                spec.commandLine(),
                "No task exists with this id."
        );
        tasks.save(data);
    }

    @Command(name = "done")
    public void done(@Parameters int id) throws ParameterException {
        Task task = tasks.get(id);
        if(task != null) task.done();
        else throw new ParameterException(spec.commandLine(), "No task exists with this id.");
    }

    @Command(name = "undone")
    public void undone(@Parameters int id) throws ParameterException {
        Task task = tasks.get(id);
        if(task != null) task.undone();
        else throw new ParameterException(spec.commandLine(), "No task exists with this id.");
    }

    @Override
    public Integer call() throws Exception {
        list();
        return 0;
    }
}
