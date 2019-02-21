package models.knapsack;

import lombok.Builder;
import lombok.Value;
import lombok.experimental.Wither;


@Value
@Wither
@Builder
public class Task {


    public enum TaskStatus {
        SUBMITTED, STARTED, COMPLETED
    }

    private Integer id;

    private TaskStatus status;

    Integer submitted;

    Integer started;

    Integer completed;

    public static Task createSubmitted() {
        return Task.builder()
                .status(TaskStatus.SUBMITTED)
                .build();
    }

}
