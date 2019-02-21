package models.knapsack;

import dto.data.ProblemData;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Wither;

@Builder
@Wither
@Data
public class Problem {

    private Integer id;

    private Integer taskId;

    ProblemData problemData;
}
