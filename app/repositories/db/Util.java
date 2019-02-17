package repositories.db;

import play.db.ConnectionCallable;
import play.db.Database;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public class Util {

    public static <T> DatabaseCaller<T> wrapCall(Database db,
                                                 DatabaseExecutionContext context) {
        return block -> CompletableFuture.supplyAsync(() -> db.withConnection(true, block), context);
    }


}
