package infrastructure.marshalling;

public interface Marshaller<I> {
    public I marshall(Object input);

    public <G> G unmarshall(I data, Class<G> clazz);
}
