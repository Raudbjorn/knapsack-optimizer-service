package tasks.models;

import database.util.ResultSetReadable;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.Wither;

import static database.util.Numbers.toLong;


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

    public static final ResultSetReadable<Task> READ_RESULT_SET =
            resultSet -> Task.builder()
                    .id(resultSet.getLong("ID"))
                    .submitted(toLong(resultSet.getObject("SUBMITTED")))
                    .started(toLong(resultSet.getObject("STARTED")))
                    .completed(toLong(resultSet.getObject("COMPLETED")))
                    .status(TaskStatus.valueOf(resultSet.getString("STATUS")))
                    .build();

}
