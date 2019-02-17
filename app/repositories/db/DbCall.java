package repositories.db;

import play.db.ConnectionCallable;
import play.db.Database;

import java.util.concurrent.CompletionStage;

@FunctionalInterface
public interface DbCall {

    <T> CompletionStage<T> call(ConnectionCallable<T> callable);


    public static CompletionStage<Boolean> transactionally(DbCall... calls) {
        return null;
    }


    default DbCall withDatabase(Database db) {
        return null;
    }

    default DbCall withExecutionContext(DatabaseExecutionContext context) {
        return null;
    }

    //CompletableFuture.supplyAsync(() -> db.withConnection(true, block), context)

}
