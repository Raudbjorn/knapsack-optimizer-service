package controllers.knapsack;

import dto.responses.TaskResponse;
import models.knapsack.Task;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import processors.TaskProcessor;
import repositories.ProblemRepository;
import repositories.TaskRepository;
import requests.ProblemRequest;

import javax.inject.Inject;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static io.vavr.Patterns.$Left;
import static io.vavr.Patterns.$Right;

public class TaskController extends Controller {


    private final TaskProcessor taskProcessor;

    @Inject
    public TaskController(TaskProcessor taskProcessor) {
        this.taskProcessor = taskProcessor;
    }

    @BodyParser.Of(BodyParser.Json.class)
    public CompletionStage<Result> createTask(Http.Request request) {
        ProblemRequest problemRequest = Json.fromJson(request.body().asJson(), ProblemRequest.class);

        /*
        System.out.println(problemRequest);
        return Match(Knapsack.solve(problemRequest.getProblem())).of(
                Case($Right($()), result -> status(OK, result.toJson())),
                Case($Left($()), error -> status(error.status, error.errorMsg))
        );
        */

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
