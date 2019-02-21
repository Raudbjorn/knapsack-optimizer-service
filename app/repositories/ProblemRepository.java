package repositories;


import dto.data.ProblemData;
import models.knapsack.Problem;
import models.knapsack.Task;
import play.db.ConnectionCallable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ProblemRepository {

    private static final String INSERT =
            "INSERT INTO PROBLEM(TASK_ID, JSON) VALUES(?, ?)";

    private static final String GET_BY_TASK_ID =
            "SELECT * FROM PROBLEM WHERE TASK_ID = ?";

    public static ConnectionCallable<Integer> saveProblem(ProblemData problem, Task task){
        return connection -> {
            PreparedStatement problemStatement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
            problemStatement.setInt(1, task.getId());
            problemStatement.setString(2, problem.toJson());
            problemStatement.executeUpdate();
            return problemStatement.getGeneratedKeys().getInt(1);
        };
    }

    public static ConnectionCallable<Problem> getProblemByTaskId(int taskId){
        return connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_BY_TASK_ID);
            preparedStatement.setInt(1, taskId);
            ResultSet resultSet = preparedStatement.executeQuery();
            return fromResultSet(resultSet);
        };
    }


    private static Problem fromResultSet(ResultSet resultSet) throws SQLException {
        ProblemData data = ProblemData.fromJson(resultSet.getString("JSON"));
        return Problem.builder()
                .id(resultSet.getInt("ID"))
                .problemData(data)
                .taskId(resultSet.getInt("TASK_ID"))
                .build();
    }



}
