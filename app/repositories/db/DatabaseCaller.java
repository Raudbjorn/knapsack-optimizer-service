package repositories.db;

import play.db.ConnectionCallable;

import java.util.concurrent.CompletionStage;

@FunctionalInterface
public interface DatabaseCaller<T> {

    CompletionStage<T> call(ConnectionCallable<T> callable);

}
