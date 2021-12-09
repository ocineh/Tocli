import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.util.concurrent.Callable;

@Command(name = "todonisator", mixinStandardHelpOptions = true, version = "todonisator 0.0.1")
public class Todo implements Callable<Integer> {
    public static void main(String[] args) {
        System.exit(new CommandLine(new Todo()).execute(args));
    }

    @Command(name = "list")
    public void list() {
        Task.getTasks().forEach(System.out::println);
    }

    @Command(name = "add")
    public void add() {
    }

    @Command(name = "update")
    public void update() {
    }

    @Command(name = "delete")
    public void delete() {
    }

    @Command(name = "done")
    public void done() {
    }

    @Command(name = "undone")
    public void undone() {
    }

    @Override
    public Integer call() throws Exception {
        return 0;
    }
}
