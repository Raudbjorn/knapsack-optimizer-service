package controllers.knapsack;

import models.knapsack.Task;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import repositories.TaskRepository;
import requests.ProblemRequest;
import solvers.Knapsack;

import javax.inject.Inject;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.concurrent.CompletionStage;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static io.vavr.Patterns.$Left;
import static io.vavr.Patterns.$Right;

public class TaskController extends Controller {

    private final TaskRepository taskRepository;

    @Inject
    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
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
        Task task = Task.createSubmitted();

        return taskRepository.saveTask(Task.createSubmitted())
                .thenApply(String::valueOf)
                .thenApply(task::withTask)
                .thenApply(Json::toJson)
                .thenApply(json -> status(ACCEPTED, json));
    }

    public CompletionStage<Result> getTask(long id) {
        return taskRepository.getTask(id).thenApply(maybeTask ->
                maybeTask.map(Json::toJson).map(json -> status(OK, json)).orElse(status(NOT_FOUND))
        );
    }

}
