package dto.responses;

import dto.data.ProblemData;
import dto.data.SolutionData;
import lombok.Data;

@Data
public class SolutionResponse {

    private String task;

    private ProblemData problemData;

    private SolutionData solutionData;

}
