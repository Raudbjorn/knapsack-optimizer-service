package repositories;

import models.knapsack.Task;
import play.db.ConnectionCallable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    public static ConnectionCallable<Integer> saveTask(Task task){
        return connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT,  Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, task.getStatus().name());
            preparedStatement.setNull(2, Types.INTEGER);
            preparedStatement.setNull(3, Types.INTEGER);
            preparedStatement.execute();

            return preparedStatement.getGeneratedKeys().getInt(1);
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

    public static ConnectionCallable<Optional<Task>> getTask(long id){
        return connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_TASK);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                return Optional.of(fromResultSet(resultSet));
            }

             return Optional.empty();
        };
    }

    private static Task fromResultSet(ResultSet resultSet) throws SQLException {
        return Task.builder()
                .id(resultSet.getInt("ID"))
                .submitted(resultSet.getLong("SUBMITTED"))
                .started((Long) resultSet.getObject("STARTED"))
                .completed((Long) resultSet.getObject("COMPLETED"))
                .status(Task.TaskStatus.valueOf(resultSet.getString("STATUS")))
                .build();
    }

}
