package infrastructure.serialization;

public interface Serializer {
    String serialize(Object obj);

    <T> T deserialize(String data, Class<T> clazz);
}
