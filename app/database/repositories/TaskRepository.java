package database.repositories;

import database.util.ResultSetProvider;
import play.db.ConnectionCallable;
import tasks.models.Problem;
import tasks.models.Task;

import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
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


    public static ConnectionCallable<Long> insertTask(Task task) {
        return connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, task.getStatus().name());
            preparedStatement.setNull(2, Types.INTEGER);
            preparedStatement.setNull(3, Types.INTEGER);
            preparedStatement.execute();

            return preparedStatement.getGeneratedKeys().getLong(1);
        };
    }

    public static ConnectionCallable<Integer> updateTaskStatus(long id, Task.TaskStatus status) {
        return connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_STATUS);
            preparedStatement.setString(1, status.name());
            preparedStatement.setLong(2, id);
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

    public static <T> ResultSetProvider<T> getAllTasks() {
        return reader -> connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL);
            return reader.read(preparedStatement.executeQuery());
        };
    }

    public static ConnectionCallable<Optional<Task>> checkForSolution(Problem problem) {
        return  connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_TASK_BY_PROBLEM);
            preparedStatement.setString(1, problem.toJson());
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next()
                    ? Optional.ofNullable(Task.READ_RESULT_SET.read(resultSet))
                    : Optional.empty();
        };
    }

}
