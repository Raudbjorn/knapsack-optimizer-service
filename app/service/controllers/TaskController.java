package service.controllers;


import io.vavr.collection.Seq;
import io.vavr.control.Try;
import io.vavr.control.Validation;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;
import service.RequestProcessor;
import service.json.data.Problem;
import service.json.requests.ProblemRequest;
import service.validation.ServiceError;
import service.validation.valid.ValidProblem;
import tasks.TaskProcessor;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static io.vavr.API.*;
import static io.vavr.Patterns.$Invalid;
import static io.vavr.Patterns.$Valid;
import static service.validation.RequestValidation.validateProblemRequest;

public class TaskController extends Controller {


    private final RequestProcessor requestProcessor;

    @Inject
    public TaskController(RequestProcessor requestProcessor) {
        this.requestProcessor = requestProcessor;
    }

    @BodyParser.Of(BodyParser.Json.class)
    public CompletionStage<Result> createTask(Http.Request request) {
        ProblemRequest problemRequest =
                Json.fromJson(request.body().asJson(), ProblemRequest.class);

        Validation<Seq<ServiceError>, ValidProblem> checkRequest =
                validateProblemRequest(problemRequest);

       return Match(checkRequest).of(
                Case($Valid($()), problem -> requestProcessor.createTask(problem)
                                .thenApply(Json::toJson)
                                .thenApply(json -> status(ACCEPTED, json))
                ),
                Case($Invalid($()), ServiceError::toResult)
        );
    }

    public CompletionStage<Result> getTask(String taskId) {
        Optional<Long> maybeId = Try.of(() -> Long.parseLong(taskId)).toJavaOptional();

        return maybeId.map(id -> requestProcessor.getTask(id)
                .thenApply(Json::toJson)
                .thenApply(Results::ok)).orElse(CompletableFuture.completedFuture(notFound()));
    }

}
