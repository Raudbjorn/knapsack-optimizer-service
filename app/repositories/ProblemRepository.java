package repositories;


import models.knapsack.Problem;
import models.knapsack.Task;
import play.db.Database;
import play.libs.Json;
import repositories.db.DatabaseCaller;
import repositories.db.DatabaseExecutionContext;
import repositories.db.Util;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.transaction.Transaction;
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

    public CompletionStage<Integer> saveProblem(Problem problem, Task task){
        return Util.<Integer> wrapCall(db, executionContext).call(connection -> {


            PreparedStatement problemStatement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
            problemStatement.setInt(1, Integer.parseInt(task.getTask()));
            problemStatement.setString(2, Json.toJson(problem).asText());
            problemStatement.executeUpdate();

            return problemStatement.getGeneratedKeys().getInt(1);
        });
    }


}
