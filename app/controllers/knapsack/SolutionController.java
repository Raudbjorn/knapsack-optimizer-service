package controllers.knapsack;

import dto.responses.SolutionResponse;
import io.vavr.control.Try;
import models.knapsack.Problem;
import models.knapsack.Solution;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import processors.TaskProcessor;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class SolutionController extends Controller {

    private final TaskProcessor taskProcessor;

    @Inject
    public SolutionController(TaskProcessor taskProcessor) {
        this.taskProcessor = taskProcessor;
    }

    public CompletionStage<Result> getSolution(String taskId) {
        int parsedId = Try.of(() -> Integer.parseInt(taskId)).getOrElse(-1);
        return taskProcessor.getSolution(parsedId).thenCompose(
                maybeSolution -> maybeSolution
                        .map(s -> makeResponse(parsedId, s))
                        .orElse(CompletableFuture.completedFuture(notFound()))
        );
    }


    private CompletionStage<Result> makeResponse(int taskId, Solution solution) {
        return taskProcessor.getProblemByTaskId(taskId).thenApply(problem -> {
            SolutionResponse result = new SolutionResponse();
            result.setTask(String.valueOf(solution.getTaskId()));

            solution.getSolutionData().setTime(solution.getTime());
            result.setSolutionData(solution.getSolutionData());

            result.setProblemData(problem.getProblemData());

            return result;
        })
                .thenApply(Json::toJson)
                .thenApply(Results::ok);
    }


}
