package service.controllers;

import io.vavr.control.Try;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import service.RequestProcessor;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class SolutionController extends Controller {

    private final RequestProcessor requestProcessor;

    @Inject
    public SolutionController(RequestProcessor requestProcessor) {
        this.requestProcessor = requestProcessor;
    }

    public CompletionStage<Result> getSolution(String taskId) {
        Optional<Long> maybeId = Try.of(() -> Long.parseLong(taskId)).toJavaOptional();

        return maybeId.map(id -> requestProcessor
                .getSolution(id)
                .thenApply(Json::toJson)
                .thenApply(Results::ok)
        ).orElse(CompletableFuture.completedFuture(notFound()));
    }
}
