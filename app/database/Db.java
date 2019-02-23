package database;

import database.context.DatabaseExecutionContext;
import play.Environment;
import play.db.ConnectionCallable;
import play.db.ConnectionRunnable;
import play.db.Database;
import play.libs.concurrent.CustomExecutionContext;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;

import static util.Throwables.propagate;

@Singleton
public class Db {

    private final Database db;
    private final DatabaseExecutionContext executionContext;

    @Inject
    public Db(Database db, DatabaseExecutionContext context, Environment environment) {
        this.db = db;
        this.executionContext = context;

            Stream.of(
                    "TASK.sql",
                    "PROBLEM.sql",
                    "SOLUTION.sql",
                    "UPDATE_STATUS_TIME.sql"
                    ).map(fName -> "/conf/database/" + fName)
                    .flatMap(propagate(dirName -> Files.walk(environment.getFile(dirName).toPath())))
                    .filter(f -> f.getFileName().toString().toLowerCase().endsWith(".sql"))
                    .map(propagate(Files::readAllBytes))
                    .map(bytes -> new String(bytes, StandardCharsets.UTF_8))
                    .forEach(sql -> db.withConnection(connection -> {
                        connection.prepareStatement(sql).executeUpdate();
                    }));
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
