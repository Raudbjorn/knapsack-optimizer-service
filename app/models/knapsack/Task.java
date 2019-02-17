package models.knapsack;

import io.vavr.control.Either;
import lombok.Data;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Data
public class Task implements ServiceModel {

    public static final Map<Long, CompletableFuture<Either<ServiceError, Solution>>> ACTIVE_TASKS = new ConcurrentHashMap<>();

    public enum TaskStatus {
        SUBMITTED, STARTED, COMPLETED
    }

    private String task;

    private TaskStatus status;

    private TimeStamps timeStamps;

    @Data
    public class TimeStamps {
        long submitted;

        Long started;

        Long completed;
    }


}
