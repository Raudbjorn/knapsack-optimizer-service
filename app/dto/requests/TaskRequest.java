package dto.requests;

import lombok.Builder;
import lombok.Value;
import lombok.experimental.Wither;
import models.knapsack.Problem;


@Value
@Wither
@Builder
public class TaskRequest {

    Problem problem;

}
