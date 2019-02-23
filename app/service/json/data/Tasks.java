package service.json.data;

import lombok.Data;
import service.json.responses.TaskResponse;

import java.util.List;

@Data
public class Tasks {

    private List<TaskResponse> submitted;
    private List<TaskResponse> started;
    private List<TaskResponse> completed;

}
