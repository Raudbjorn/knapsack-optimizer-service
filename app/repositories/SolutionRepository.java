package repositories;

import models.knapsack.Solution;
import play.db.ConnectionCallable;
import play.libs.Json;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Optional;

public class SolutionRepository {

    private static final String INSERT =
            "INSERT INTO SOLUTION(JSON, TASK_ID, PROBLEM_ID) VALUES (?, ?, (SELECT MAX(ID) FROM PROBLEM WHERE TASK_ID = ?))";

    private static final String GET_BY_TASK_ID =
            "SELECT JSON FROM SOLUTION WHERE TASK_ID = ?";

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

    public static ConnectionCallable<Optional<Solution>> getSolutionByTaskId(int taskId){
        return connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_BY_TASK_ID);
            preparedStatement.setInt(1, taskId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                String  json = resultSet.getString("JSON");
                return Optional.of(Json.fromJson(Json.parse(json), Solution.class));
            } else {
                return Optional.empty();
            }
        };
    }
}
