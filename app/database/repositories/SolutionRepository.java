package database.repositories;

import database.util.ResultSetProvider;
import play.db.ConnectionCallable;
import play.libs.Json;
import tasks.models.Solution;

import java.sql.PreparedStatement;
import java.sql.Statement;

public class SolutionRepository {

    private static final String INSERT =
            "INSERT INTO SOLUTION(TASK_ID, PROBLEM_ID, ITEMS) VALUES (?, ?, ?)";

    private static final String GET_BY_TASK_ID =
            "SELECT * FROM SOLUTION WHERE TASK_ID = ?";

    private static final String GET_SOLUTION_DATA =
        "SELECT T.ID AS TASK_ID,\n" +
        "       T.STARTED AS TASK_STARTED,\n" +
        "       T.COMPLETED AS TASK_COMPLETED,\n" +
        "       P.JSON AS PROBLEM_JSON,\n" +
        "       S.ITEMS AS SOLUTION_ITEMS\n" +
        "FROM SOLUTION S\n" +
        "JOIN TASK T ON T.ID = S.TASK_ID\n" +
        "JOIN PROBLEM P on S.PROBLEM_ID = P.ID\n" +
        "WHERE T.id = ?";



    public static ConnectionCallable<Integer> insertSolution(Solution solution){
        return connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setLong(1, solution.getTaskId());
            preparedStatement.setLong(2, solution.getProblemId());
            preparedStatement.setString(3, Json.toJson(solution.getItems()).toString());
            preparedStatement.executeUpdate();
            return preparedStatement.getGeneratedKeys().getInt(1);
        };
    }

    /*
    public static ConnectionCallable<Optional<Solution>> getSolutionByTaskId(long taskId){
        return connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_BY_TASK_ID);
            preparedStatement.setLong(1, taskId);
            ResultSet resultSet = preparedStatement.executeQuery();
            return !resultSet.next() ? Optional.empty() : Optional.of(fromResultSet(resultSet));
        };
    }
    */



    public static <T> ResultSetProvider<T> getSolutionResponseDataByTaskId(long taskId){
        return reader -> connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_SOLUTION_DATA);
            preparedStatement.setLong(1,taskId);
            return reader.read(preparedStatement.executeQuery());
        };
    }

}
