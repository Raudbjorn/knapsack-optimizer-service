package repositories;

import io.vavr.control.Try;
import models.knapsack.Task;
import play.db.ConnectionCallable;
import play.db.Database;
import repositories.db.DatabaseExecutionContext;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;


@Singleton
public class TaskRepository {

    private Database db;
    private DatabaseExecutionContext executionContext;

    private static final String INSERT =
            "INSERT INTO TASK(STATUS, SUBMITTED, STARTED, COMPLETED) VALUES(?, ?, ?, ?)";

    private static final String UPDATE_STATUS =
            "UPDATE TASK SET STATUS = ? WHERE ID = ?";

    private static final String GET_TASK =
            "SELECT * FROM TASK WHERE ID = ?";

    @Inject
    public TaskRepository(Database db, DatabaseExecutionContext context) {
        this.db = db;
        this.executionContext = context;
    }

    private <T> CompletionStage<T> eventually(ConnectionCallable<T> block){
        return CompletableFuture.supplyAsync(() -> db.withConnection(true, block), executionContext);
    }

    public CompletionStage<Integer> saveTask(Task task){
        return eventually(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT,  Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, task.getStatus().name());
            preparedStatement.setLong(2, task.getTimeStamps().getSubmitted());
            preparedStatement.setNull(3, Types.INTEGER);
            preparedStatement.setNull(4, Types.INTEGER);

            preparedStatement.execute();

            return preparedStatement.getGeneratedKeys().getInt(1);
        });
    }

    public CompletionStage<Integer> uppdateTaskStatus(long id, Task.TaskStatus status){
        return eventually(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_STATUS);
            preparedStatement.setString(1, status.name());
            preparedStatement.setInt(2, (int) id);
            return preparedStatement.executeUpdate();
        });
    }

    public CompletionStage<Optional<Task>> getTask(long id){
        return eventually(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_TASK);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                Task.TimeStamps timeStamps = Task.TimeStamps.builder()
                        .submitted(resultSet.getLong("SUBMITTED"))
                        .started((Long) resultSet.getObject("STARTED"))
                        .completed((Long) resultSet.getObject("COMPLETED"))
                        .build();

                return Optional.of(Task.builder()
                        .task(String.valueOf(resultSet.getInt("ID")))
                        .timeStamps(timeStamps)
                        .status(Task.TaskStatus.valueOf(resultSet.getString("STATUS")))
                        .build());
            }

             return Optional.empty();
        });
    }

}
