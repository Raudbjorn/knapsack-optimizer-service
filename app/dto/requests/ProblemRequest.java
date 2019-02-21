package dto.requests;

import dto.data.ProblemData;
import lombok.Data;
import models.knapsack.Problem;

@Data
public class ProblemRequest {

    ProblemData problem;

}
