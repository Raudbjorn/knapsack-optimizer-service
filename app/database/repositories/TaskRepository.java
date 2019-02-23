package database.repositories;

import database.util.ResultSetProvider;
import dto.data.ProblemData;
import database.util.ResultSetReadable;
import tasks.models.Task;
import play.db.ConnectionCallable;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class TaskRepository {

    private static final String INSERT =
            "INSERT INTO TASK(STATUS, STARTED, COMPLETED) VALUES(?, ?, ?)";

    private static final String UPDATE_STATUS =
            "UPDATE TASK SET STATUS = ? WHERE ID = ?";

    private static final String GET_TASK =
            "SELECT * FROM TASK WHERE ID = ?";

    private static final String GET_ALL = "SELECT * FROM TASK";

    private static final String GET_TASK_BY_PROBLEM =
            "SELECT TASK.* FROM TASK\n" +
            "JOIN SOLUTION S on TASK.ID = S.TASK_ID\n" +
            "JOIN PROBLEM P on S.PROBLEM_ID = P.ID\n" +
            "WHERE P.JSON = ?";


    public static ConnectionCallable<Long> saveTask(Task task){
        return connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT,  Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, task.getStatus().name());
            preparedStatement.setNull(2, Types.INTEGER);
            preparedStatement.setNull(3, Types.INTEGER);
            preparedStatement.execute();

            return preparedStatement.getGeneratedKeys().getLong(1);
        };
    }

    public static ConnectionCallable<Integer> updateTaskStatus(long id, Task.TaskStatus status){
        return connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_STATUS);
            preparedStatement.setString(1, status.name());
            preparedStatement.setInt(2, (int) id);
            return preparedStatement.executeUpdate();
        };
    }

    public static <T> ResultSetProvider<T> getTask(long id) {
        return reader -> connection -> {
          PreparedStatement preparedStatement = connection.prepareStatement(GET_TASK);
          preparedStatement.setLong(1, id);
          return reader.read(preparedStatement.executeQuery());
        };
    }


    /*
    public static ConnectionCallable<Optional<Task>> getTask(long id){
        return connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_TASK);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next()
                    ? Optional.of(fromResultSet(resultSet))
                    : Optional.empty();
        };
    }
    */

    public static ConnectionCallable<List<Task>> getAll(){
        return connection -> {
            List<Task> result = new ArrayList<>();
            PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                result.add(fromResultSet(resultSet));
            }
            return result;
        };
    }

    public static <T> ConnectionCallable<T> getAllTasks(ResultSetReadable<T> reader){
        return connection ->{
            PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL);
            return reader.read(preparedStatement.executeQuery());
        };
    }

    public static ConnectionCallable<Optional<Task>> checkForSoluiton(ProblemData problem){
        return connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_TASK_BY_PROBLEM);
            preparedStatement.setString(1, problem.toJson());
            ResultSet resultSet = preparedStatement.executeQuery();
            Optional<Task> result = !resultSet.next()
                    ? Optional.empty()
                    : Optional.of(TaskRepository.fromResultSet(resultSet));
            return result;
        };
    }

    private static Task fromResultSet(ResultSet resultSet) throws SQLException {
        return Task.builder()
                .id(resultSet.getLong("ID"))
                .submitted((Long) resultSet.getObject("SUBMITTED"))
                .started((Long) resultSet.getObject("STARTED"))
                .completed((Long) resultSet.getObject("COMPLETED"))
                .status(Task.TaskStatus.valueOf(resultSet.getString("STATUS")))
                .build();
    }

}
