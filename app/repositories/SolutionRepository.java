package repositories;

import models.knapsack.Solution;
import play.db.ConnectionCallable;
import play.libs.Json;

import java.sql.PreparedStatement;
import java.sql.Statement;

public class SolutionRepository {

    private static final String INSERT =
            "INSERT INTO SOLUTION(JSON, PROBLEM_ID, TASK_ID) VALUES (?, ?, ?)";

    public static ConnectionCallable<Integer> insertSolution(Solution solution, int problemId, int taskId){
        return connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, Json.toJson(solution).toString());
            preparedStatement.setInt(2, problemId);
            preparedStatement.setInt(3, taskId);
            preparedStatement.executeUpdate();
            return preparedStatement.getGeneratedKeys().getInt(1);
        };
    }
}
