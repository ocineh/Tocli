import java.io.*;
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
}
