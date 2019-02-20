package repositories;

import models.knapsack.Solution;
import play.db.ConnectionCallable;
import play.libs.Json;

import java.sql.PreparedStatement;
import java.sql.Statement;

public class SolutionRepository {

    private static final String INSERT =
            "INSERT INTO SOLUTION(JSON, TASK_ID, PROBLEM_ID) VALUES (?, ?, (SELECT MAX(ID) FROM PROBLEM WHERE TASK_ID = ?))";

    public static ConnectionCallable<Integer> insertSolution(Solution solution, int taskId){
        return connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, Json.toJson(solution).toString());
            preparedStatement.setInt(2, taskId);
            preparedStatement.setInt(3, taskId);
            preparedStatement.executeUpdate();
            return preparedStatement.getGeneratedKeys().getInt(1);
        };
    }
}
