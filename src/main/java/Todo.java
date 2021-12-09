import picocli.CommandLine;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.*;

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
    public void update(@Parameters int id, @Parameters String title) {
        Task task = Task.getTaskById(id);
        if(task == null) throw new ParameterException(spec.commandLine(), "No task exists with this id.");
        if(title == null || title.isEmpty() || title.isBlank())
            throw new ParameterException(spec.commandLine(), "The new task title cannot be empty or contain only spaces.");
        task.setTitle(title);
    }

    @Command(name = "delete")
    public void delete(@Parameters int id) {
        if(!Task.deleteTask(id)) throw new ParameterException(spec.commandLine(), "No task exists with this id.");
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
