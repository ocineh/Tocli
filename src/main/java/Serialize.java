import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.lang.reflect.Type;
import java.util.Base64;

public interface Serialize extends java.io.Serializable {
    static <T extends java.io.Serializable> T fromBase64(String str)
    throws IOException, ClassNotFoundException {
        byte[] bytes = Base64.getDecoder().decode(str);
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
        return (T) ois.readObject();
    }

    static <T extends java.io.Serializable> String toBase64(T obj) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(stream);
        oos.writeObject(obj);
        oos.close();
        return Base64.getEncoder().encodeToString(stream.toByteArray());
    }

    static <T extends java.io.Serializable> String toJson(T obj, boolean pretty) {
        GsonBuilder builder = new GsonBuilder();
        if(pretty) builder.setPrettyPrinting();
        return builder.create().toJson(obj);
    }

    static <T extends java.io.Serializable> T fromJson(JsonReader json, Type type) {
        return new Gson().fromJson(json, type);
    }

    static <T extends java.io.Serializable> T fromJson(JsonReader json, Class<T> clazz) {
        return new Gson().fromJson(json, clazz);
    }
}
