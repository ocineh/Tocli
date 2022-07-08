package cli;

import models.Data;
import picocli.CommandLine.Model;
import picocli.CommandLine.ParameterException;
import picocli.CommandLine.ParentCommand;

public abstract class SubCommand implements Runnable {
    private @ParentCommand Tocli tocli;

    public SubCommand() {
    }

    public SubCommand(Tocli tocli) {
        this.tocli = tocli;
    }

    public Model.CommandSpec getSpec() {
        return tocli.getSpec();
    }

    public String getTodoListName() {
        return tocli.getTodoListName();
    }

    public Data getData() {
        return tocli.getData();
    }

    public void save() {
        tocli.save();
    }

    public void throwException(String message) throws ParameterException {
        throw new ParameterException(getSpec().commandLine(), message);
    }
}
