package repositories;


import models.knapsack.Problem;
import models.knapsack.Task;
import play.db.ConnectionCallable;
import play.libs.Json;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class ProblemRepository {

    private static final String INSERT =
            "INSERT INTO PROBLEM(TASK_ID, JSON) VALUES(?, ?)";

    private static final String GET_BY_TASK_ID =
            "SELECT JSON FROM PROBLEM WHERE TASK_ID = ?";

    public static ConnectionCallable<Integer> saveProblem(Problem problem, Task task){
        return connection -> {
            PreparedStatement problemStatement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
            problemStatement.setInt(1, task.getId());
            problemStatement.setString(2, Json.toJson(problem).toString());
            problemStatement.executeUpdate();
            return problemStatement.getGeneratedKeys().getInt(1);
        };
    }

    public static ConnectionCallable<Problem> getProblemByTaskId(int taskId){
        return connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_BY_TASK_ID);
            preparedStatement.setInt(1, taskId);
            ResultSet resultSet = preparedStatement.executeQuery();
            return Json.fromJson(Json.parse(resultSet.getString("JSON")), Problem.class);
        };
    }


}
