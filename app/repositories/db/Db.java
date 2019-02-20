package repositories.db;

import contexts.DatabaseExecutionContext;
import play.db.ConnectionCallable;
import play.db.ConnectionRunnable;
import play.db.Database;
import play.libs.concurrent.CustomExecutionContext;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

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

    public void run(ConnectionRunnable call) {
        CompletableFuture.runAsync(() -> db.withConnection(call), executionContext);
    }

    public <T> CompletionStage<T> call(ConnectionCallable<T> call, CustomExecutionContext context) {
        return CompletableFuture.supplyAsync(() -> db.withConnection(call), context);
    }

    public void run(ConnectionRunnable call, CustomExecutionContext context) {
        CompletableFuture.runAsync(() -> db.withConnection(call), context);
    }

}
