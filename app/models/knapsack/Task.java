package models.knapsack;

import io.vavr.control.Either;
import lombok.*;
import lombok.experimental.Wither;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Value
@Wither
@Builder
public class Task implements ServiceModel {

    //public static final Map<Long, CompletableFuture<Either<ServiceError, Solution>>> ACTIVE_TASKS = new ConcurrentHashMap<>();

    public enum TaskStatus {
        SUBMITTED, STARTED, COMPLETED
    }

    private String task;

    private TaskStatus status;

    private TimeStamps timeStamps;

    @Value
    @Wither
    @Builder
    public static class TimeStamps {
        long submitted;

        Long started;

        Long completed;
    }

    public static Task createSubmitted(){
        return Task.builder()
                .status(Task.TaskStatus.SUBMITTED)
                .timeStamps(TimeStamps
                        .builder()
                        .submitted(LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond())
                        .started(null)
                        .completed(null)
                        .build()
                )
                .build();
    }


}
