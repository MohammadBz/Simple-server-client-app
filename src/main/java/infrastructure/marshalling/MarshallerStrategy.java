 package infrastructure.marshalling;

import java.util.Objects;

public final class MarshallerStrategy {
    private static volatile Marshaller<String> Globalmarshaller = JacksonMarshaller.getInstance();

    private MarshallerStrategy() {
    }

    public static Marshaller<String> getMarshaller() {
        return Globalmarshaller;
    }
    public static void setMarshaller(Marshaller<String> newStrategy) {
        Globalmarshaller = Objects.requireNonNull(newStrategy, "Marshaller strategy cannot be null");
    }

}
