package tasks.models;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Streams;
import database.util.ResultSetReadable;
import io.vavr.collection.Stream;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.Wither;
import play.libs.Json;

import java.math.BigInteger;
import java.util.Arrays;

@Value
@Wither
@Builder
public class Problem {

    private Long id;

    private Long taskId;

    private long capacity;

    private long[] weights;

    private long[] values;

    public String toJson(){
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("capacity", capacity);

        ArrayNode weightNode = node.putArray( "weights");
        Arrays.stream(weights).forEach(weightNode::add);

        ArrayNode valuesNode = node.putArray( "values");
        Arrays.stream(values).forEach(valuesNode::add);

        return Json.stringify(node);
    }

    public static Problem fromJson(service.json.data.Problem jsonProblem) {
        return builder()
                .capacity(jsonProblem.getCapacity().longValue())
                .weights(jsonProblem.getWeights()
                        .stream()
                        .mapToLong(BigInteger::longValue)
                        .toArray())
                .values(jsonProblem.getValues()
                        .stream()
                        .mapToLong(BigInteger::longValue)
                        .toArray())
                .build();
    }

    public static final ResultSetReadable<Problem> READ_RESULT_SET =
            resultSet -> {
                JsonNode node = Json.parse(resultSet.getString("JSON"));

                long capacity = node.get("capacity").asLong();

                long[] weights = Streams.stream(node.withArray("weights").elements())
                        .mapToLong(JsonNode::longValue)
                        .toArray();

                long[] values = Streams.stream(node.withArray("values").elements())
                        .mapToLong(JsonNode::longValue)
                        .toArray();

                return builder()
                        .id(resultSet.getLong("ID"))
                        .taskId(resultSet.getLong("TASK_ID"))
                        .capacity(capacity)
                        .weights(weights)
                        .values(values)
                        .build();
            };

}
