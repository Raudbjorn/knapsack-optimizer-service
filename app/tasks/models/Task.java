package tasks.models;

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

    private Long id;

    private TaskStatus status;

    Long submitted;

    Long started;

    Long completed;

    public static Task createSubmitted() {
        return Task.builder()
                .status(TaskStatus.SUBMITTED)
                .build();
    }

}
