package service.json.responses;

import database.util.ResultSetReadable;
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
                solutionResult.setItems(resultSet.getString("SOLUTION_ITEMS"));
                solutionResult.setTime(resultSet.getLong("TASK_COMPLETED") - resultSet.getLong("TASK_STARTED"));

                SolutionResponse result = new SolutionResponse();
                result.setTask(resultSet.getString("TASK_ID"));
                result.setProblem(problemResult);
                return result;
            };

}
