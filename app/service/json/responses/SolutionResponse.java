package service.json.responses;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import database.util.ResultSetReadable;
import io.vavr.collection.Stream;
import lombok.Data;
import play.libs.Json;
import service.json.data.Problem;
import service.json.data.Solution;

@Data
public class SolutionResponse {

    private String task;

    private Problem problem;

    private Solution solution;

    public static ResultSetReadable<SolutionResponse> READ_RESULT_SET =
            resultSet -> {
                Problem problemResult =
                        Json.fromJson(Json.parse(resultSet.getString("PROBLEM_JSON")), Problem.class);

                Solution solutionResult = new Solution();
                ArrayNode items = (ArrayNode) Json.parse(resultSet.getString("SOLUTION_ITEMS"));
                solutionResult.setItems(Stream.ofAll(items).toJavaStream().mapToLong(JsonNode::asLong).toArray());
                solutionResult.setTime(resultSet.getLong("TASK_COMPLETED") - resultSet.getLong("TASK_STARTED"));

                SolutionResponse result = new SolutionResponse();
                result.setTask(resultSet.getString("TASK_ID"));
                result.setProblem(problemResult);
                result.setSolution(solutionResult);
                return result;
            };

}
