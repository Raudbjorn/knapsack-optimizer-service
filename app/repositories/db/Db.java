package repositories.db;

import contexts.DatabaseExecutionContext;
import play.db.ConnectionCallable;
import play.db.Database;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

@Singleton
public class Db {

    private final Database db;
    private final DatabaseExecutionContext executionContext;

    @Inject
    public Db(Database db, DatabaseExecutionContext context) {
        this.db = db;
        this.executionContext = context;
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
