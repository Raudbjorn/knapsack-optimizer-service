package controllers.knapsack;

import io.vavr.control.Try;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import processors.TaskProcessor;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public class SolutionController extends Controller {

    private final TaskProcessor taskProcessor;

    @Inject
    public SolutionController(TaskProcessor taskProcessor) {
        this.taskProcessor = taskProcessor;
    }

    public CompletionStage<Result> getSolution(String taskId) {
        int parsedId = Try.of(() -> Integer.parseInt(taskId)).getOrElse(-1);
        return taskProcessor.getSolution(parsedId)
                .thenApply(maybeSolution -> maybeSolution
                                .map(Json::toJson)
                                .map(Results::ok)
                                .orElse(notFound())
                );
    }

}
