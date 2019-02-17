package repositories;


import models.knapsack.Problem;
import play.db.Database;
import play.libs.Json;
import repositories.db.DatabaseCaller;
import repositories.db.DatabaseExecutionContext;
import repositories.db.Util;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.concurrent.CompletionStage;

import static repositories.db.Util.wrapCall;

@Singleton
public class ProblemRepository {

    private Database db;
    private DatabaseExecutionContext executionContext;

    private static final String INSERT =
            "INSERT INTO PROBLEM(TASK_ID, JSON) VALUES(?, ?)";


    @Inject
    public ProblemRepository(Database db, DatabaseExecutionContext context) {
        this.db = db;
        this.executionContext = context;
    }

    public CompletionStage<Integer> saveProblem(Problem problem, String task){
        return Util.<Integer> wrapCall(db, executionContext).call(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, task);
            preparedStatement.setString(2, Json.toJson(problem).asText());
            preparedStatement.executeUpdate();
            return preparedStatement.getGeneratedKeys().getInt(1);
        });
    }


}
