import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.ParameterException;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Spec;

import java.util.concurrent.Callable;

@Command(name = "todonisator", mixinStandardHelpOptions = true, version = "todonisator 0.0.1")
public class Todo implements Callable<Integer> {
    @Spec CommandSpec spec;

    public static void main(String[] args) {
        System.exit(new CommandLine(new Todo()).execute(args));
    }

    @Command(name = "list")
    public void list() {
        Task.getTasks().forEach(System.out::println);
    }

    @Command(name = "add")
    public void add(@Parameters String title) {
        Task.addTask(new Task(title));
    }

    @Command(name = "update")
    public void update() {
    }

    @Command(name = "delete")
    public void delete() {
    }

    @Command(name = "done")
    public void done(@Parameters int id) throws ParameterException {
        for(Task task : Task.getTasks()) {
            if(task.getId() == id) {
                task.done();
                return;
            }
        }
        throw new ParameterException(spec.commandLine(), "No task exists with this id.");
    }

    @Command(name = "undone")
    public void undone(@Parameters int id) throws ParameterException {
        for(Task task : Task.getTasks()) {
            if(task.getId() == id) {
                task.undone();
                return;
            }
        }
        throw new ParameterException(spec.commandLine(), "No task exists with this id.");
    }

    @Override
    public Integer call() throws Exception {
        return 0;
    }
}
