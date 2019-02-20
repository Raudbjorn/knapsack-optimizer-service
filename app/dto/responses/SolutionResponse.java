package dto.responses;

import lombok.Builder;
import lombok.Value;
import lombok.experimental.Wither;
import models.knapsack.Problem;
import models.knapsack.Solution;

@Value
@Wither
@Builder
public class SolutionResponse {

    private String task;

    private Problem problem;

    private Solution solution;

}
