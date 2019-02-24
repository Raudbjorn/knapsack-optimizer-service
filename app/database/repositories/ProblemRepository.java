package database.repositories;


import play.db.ConnectionCallable;
import tasks.models.Problem;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Optional;

import static database.util.ResultSetReadable.maybe;

public class ProblemRepository {

    private static final String INSERT =
            "INSERT INTO PROBLEM(TASK_ID, JSON) VALUES(?, ?)";

    private static final String GET_BY_TASK_ID =
            "SELECT * FROM PROBLEM WHERE TASK_ID = ?";

    public static ConnectionCallable<Problem> saveProblem(Problem problem, long taskId){
        return connection -> {
            PreparedStatement problemStatement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
            problemStatement.setLong(1, taskId);
            problemStatement.setString(2, problem.toJson());
            problemStatement.executeUpdate();
            return problem.withId(problemStatement.getGeneratedKeys().getLong(1));
        };
    }

    public static ConnectionCallable<Optional<Problem>> getProblemByTaskId(long taskId){
        return connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_BY_TASK_ID);
            preparedStatement.setLong(1, taskId);
            ResultSet resultSet = preparedStatement.executeQuery();
            return maybe(Problem.READ_RESULT_SET).read(resultSet);
        };
    }
}
