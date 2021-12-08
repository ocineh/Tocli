import picocli.CommandLine.Command;

import java.util.concurrent.Callable;

@Command(name = "todonisator", mixinStandardHelpOptions = true, version = "todonisator 0.0.1")
public class Todo implements Callable<Integer> {
    @Command(name = "list")
    public void list() {
    }

    @Command(name = "add")
    public void add() {
    }

    @Command(name = "update")
    public void update() {
    }

    @Command(name = "delete")
    public void delete(){
    }

    @Command(name = "done")
    public void done(){
    }

    @Command(name = "undone")
    public void undone(){
    }

    @Override
    public Integer call() throws Exception {
        return 0;
    }
}
