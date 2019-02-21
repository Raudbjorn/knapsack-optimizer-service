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

    Long submitted;

    Long started;

    Long completed;

    public static Task createSubmitted() {
        return Task.builder()
                .status(Task.TaskStatus.SUBMITTED)
                .build();
    }

}
