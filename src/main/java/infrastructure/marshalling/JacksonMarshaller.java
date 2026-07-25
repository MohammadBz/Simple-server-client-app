package infrastructure.marshalling;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;
import exception.technical.JsonDeserializationException;
import exception.technical.JsonSerializationException;


public class JacksonMarshaller implements Marshaller<String> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    private JacksonMarshaller() {
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    private static class Holder {
        private static final JacksonMarshaller INSTANCE = new JacksonMarshaller();
    }

    public static JacksonMarshaller getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public String marshall(Object input) {
        try {
            return objectMapper.writeValueAsString(input);
        } catch (JsonProcessingException e) {
            throw new JsonSerializationException(input.getClass().getSimpleName(), e);
        }
    }

    @Override
    public <G> G unmarshall(String json, Class<G> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            throw new JsonDeserializationException("Failed to unmarshall to " + clazz.getSimpleName(), e);
        }
    }
}