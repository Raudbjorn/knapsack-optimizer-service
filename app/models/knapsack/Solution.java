package models.knapsack;

import dto.data.SolutionData;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.Wither;

@Value
@Builder
@Wither
public class Solution {

    private Integer id;

    private Integer problemId;

    private Integer taskId;

    SolutionData solutionData;

    private Integer time;
}
