package repositories.db;

import contexts.DatabaseExecutionContext;
import play.db.ConnectionCallable;
import play.db.Database;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toMap;
import static util.Throwables.propagate;

@Singleton
public class Db {

    /*

    CREATE TRIGGER UPDATE_STATUS_TIME AFTER UPDATE OF STATUS ON TASK
  FOR EACH ROW
BEGIN
  UPDATE TASK SET SUBMITTED = strftime('%s', 'now') WHERE lower(status) = 'submitted' AND SUBMITTED IS NULL AND ID = NEW.ID;
  UPDATE TASK SET STARTED = strftime('%s', 'now') WHERE lower(status) = 'started' AND STARTED IS NULL AND ID = NEW.ID;
  UPDATE TASK SET COMPLETED = strftime('%s', 'now') WHERE lower(status) = 'completed' AND COMPLETED IS NULL AND ID = NEW.ID;
END;

SELECT * FROM PROBLEM;

SELECT * FROM SOLUTION;

SELECT * FROM TASK;
     */

    private final Database db;
    private final DatabaseExecutionContext executionContext;

    @Inject
    public Db(Database db, DatabaseExecutionContext context) {
        this.db = db;
        this.executionContext = context;
    }

    public CompletionStage<Map<Integer, ?>> transactionally(ConnectionCallable<?>... calls) {
        return CompletableFuture.supplyAsync(() -> db.withConnection(connection -> {
            connection.setAutoCommit(false);
            Map<Integer, ?> result = IntStream
                    .range(0, calls.length)
                    .boxed()
                    .collect(
                            toMap(
                                    propagate(i -> i),
                                    propagate(i -> calls[i].call(connection))
                            )
                    );
            connection.commit();
            return result;
        }), executionContext);
    }

    public <T> CompletionStage<T> call(ConnectionCallable<T> call) {
        return CompletableFuture.supplyAsync(() -> db.withConnection(call), executionContext);
    }



    public <T,K> CompletionStage<K> chain(ConnectionCallable<T> block, Function<T, ConnectionCallable<K>> chainFunction){
        return CompletableFuture.supplyAsync(() -> db.withConnection(connection -> {
            connection.setAutoCommit(false);
            T value = block.call(connection);
            K result = chainFunction.apply(value).call(connection);
            connection.commit();
            return result;
        }), executionContext);
    }
}
