package models;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

public class Data implements Serialize {
    private HashMap<String, TodoList> todoLists;

    public Data() {
        todoLists = new HashMap<>();
    }

    public void remove(String todoListName) {
        todoLists.remove(todoListName);
    }

    public TodoList get(String todoListName) {
        if(!todoLists.containsKey(todoListName))
            todoLists.put(todoListName, new TodoList());
        return todoLists.get(todoListName);
    }

    public void merge(Data data) {
        for(String todoListName: data.todoLists.keySet()) {
            todoLists.putIfAbsent(todoListName, new TodoList());
            todoLists.get(todoListName).addAll(data.todoLists.get(todoListName));
        }
    }

    public void load(Path path) {
        try {
            String content = Files.readString(path);
            Data data = Serialize.fromBase64(content);
            merge(data);
        } catch(IOException | ClassNotFoundException ignored) {}
    }

    public void save(Path path) throws IOException {
        Files.writeString(path, Serialize.toBase64(this));
    }
}
