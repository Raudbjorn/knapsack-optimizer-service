package service.json.responses;

import lombok.Data;
import database.util.ResultSetReadable;
import service.json.data.Tasks;
import service.json.data.TimeStamps;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

@Data
public class TasksResponse {

    private Tasks tasks;

    public static final ResultSetReadable<TasksResponse> READ_RESULT_SET =
            resultSet -> {
                List<TaskResponse> allTasks = new ArrayList<>();
                while (resultSet.next()) {
                    allTasks.add(TaskResponse.READ_RESULT_SET.read(resultSet));
                }
                Map<String, List<TaskResponse>> groupedByStatus =
                        allTasks.stream().collect(groupingBy(TaskResponse::getStatus));

                Tasks tasks = new Tasks();
                tasks.setSubmitted(groupedByStatus.getOrDefault("submitted", new ArrayList<>()));
                tasks.setStarted(groupedByStatus.getOrDefault("started", new ArrayList<>()));
                tasks.setCompleted(groupedByStatus.getOrDefault("completed", new ArrayList<>()));

                TasksResponse result = new TasksResponse();
                result.setTasks(tasks);
                return result;
            };
}
