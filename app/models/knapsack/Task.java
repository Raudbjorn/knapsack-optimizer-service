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
public class Task {


    public enum TaskStatus {
        SUBMITTED, STARTED, COMPLETED
    }

    private Integer id;

    private TaskStatus status;

    long submitted;

    Long started;

    Long completed;

    public static Task createSubmitted() {
        return Task.builder()
                .status(Task.TaskStatus.SUBMITTED)
                .submitted(LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond())
                .started(null)
                .completed(null)
                .build();
    }

}
