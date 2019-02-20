package controllers.knapsack;

import dto.responses.TaskResponse;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import processors.TaskProcessor;
import requests.ProblemRequest;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

public class TaskController extends Controller {


    private final TaskProcessor taskProcessor;

    @Inject
    public TaskController(TaskProcessor taskProcessor) {
        this.taskProcessor = taskProcessor;
    }

    @BodyParser.Of(BodyParser.Json.class)
    public CompletionStage<Result> createTask(Http.Request request) {
        ProblemRequest problemRequest = Json.fromJson(request.body().asJson(), ProblemRequest.class);

        return taskProcessor
                .submitProblem(problemRequest.getProblem())
                .thenApply(TaskResponse::fromTask)
                .thenApply(Json::toJson)
                .thenApply(json -> status(ACCEPTED, json));
    }

    public CompletionStage<Result> getTask(long id) {
        CompletionStage<Optional<Result>> maybeResult =
                taskProcessor
                .getTask(id)
                .thenApply(maybeTask ->
                        maybeTask
                        .map(TaskResponse::fromTask)
                        .map(Json::toJson)
                        .map(json -> status(OK, json))
                );
        return maybeResult.thenApply(maybeResponse -> maybeResponse.orElse(status(NOT_FOUND)));
    }

}
