package dto.requests;

import lombok.Builder;
import lombok.Value;
import lombok.experimental.Wither;
import models.knapsack.Problem;
import models.knapsack.Task;


@Value
@Wither
@Builder
public class TaskRequest {

    Problem problem;

}
