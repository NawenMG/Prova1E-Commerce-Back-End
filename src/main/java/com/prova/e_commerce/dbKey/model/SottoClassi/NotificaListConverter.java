/* import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAttributeConverter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbConverter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

@DynamoDbConverter
public class NotificaListConverter implements DynamoDbAttributeConverter<List<Notifica>> {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String transformFrom(List<Notifica> input) {
        try {
            return objectMapper.writeValueAsString(input);
        } catch (IOException e) {
            throw new RuntimeException("Error serializing List<Notifica>", e);
        }
    }

    @Override
    public List<Notifica> transformTo(String input) {
        try {
            return objectMapper.readValue(input, new TypeReference<List<Notifica>>() {});
        } catch (IOException e) {
            throw new RuntimeException("Error deserializing List<Notifica>", e);
        }
    }
}
 */