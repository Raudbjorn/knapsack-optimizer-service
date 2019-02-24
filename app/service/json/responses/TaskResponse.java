package service.json.responses;

import database.util.ResultSetReadable;
import lombok.Data;
import service.json.data.TimeStamps;

import static database.util.Numbers.toLong;

@Data
public class TaskResponse {

    private String task;

    private String status;

    private TimeStamps timeStamps;

    public static ResultSetReadable<TaskResponse> READ_RESULT_SET =
            resultSet -> {
                TaskResponse result = new TaskResponse();

                result.setTask(String.valueOf(resultSet.getLong("ID")));
                result.setStatus(resultSet.getString("STATUS").toLowerCase());

                TimeStamps timeStamps = new TimeStamps();
                timeStamps.setSubmitted(toLong(resultSet.getObject("SUBMITTED")));
                timeStamps.setStarted(toLong(resultSet.getObject("STARTED")));
                timeStamps.setCompleted(toLong((resultSet.getObject("COMPLETED"))));

                result.setTimeStamps(timeStamps);

                return result;
            };

    public static TaskResponse fromTask(tasks.models.Task task) {
        TaskResponse result = new TaskResponse();

        TimeStamps timeStamps = new TimeStamps();
        timeStamps.setSubmitted(task.getSubmitted());
        timeStamps.setStarted(task.getStarted());
        timeStamps.setCompleted(task.getCompleted());

        result.setTimeStamps(timeStamps);
        result.setTask(String.valueOf(task.getId()));
        result.setStatus(task.getStatus().name().toLowerCase());

        return result;
    }
}
