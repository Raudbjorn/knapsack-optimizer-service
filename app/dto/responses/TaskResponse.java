package dto.responses;

import lombok.Builder;
import lombok.Value;
import lombok.experimental.Wither;
import models.knapsack.Task;

import java.util.List;
import java.util.stream.Collectors;

@Value
@Wither
@Builder
public class TaskResponse {

    private String task;

    private String status;

    private TaskResponse.TimeStamps timestamps;

    @Value
    @Wither
    @Builder
    private static class TimeStamps {
        Integer submitted;

        Integer started;

        Integer completed;
    }

    public static TaskResponse fromTask(Task task) {
        TimeStamps timeStamps = TimeStamps.builder()
                .submitted(task.getSubmitted())
                .started(task.getStarted())
                .completed(task.getCompleted())
                .build();

        return TaskResponse.builder()
                .task(String.valueOf(task.getId()))
                .status(task.getStatus().name().toLowerCase())
                .timestamps(timeStamps)
                .build();
    }

    public static List<TaskResponse> fromTasks(List<Task> tasks) {
        return tasks.stream().map(TaskResponse::fromTask).collect(Collectors.toList());
    }

}
