package service.controllers;

import akka.actor.CoordinatedShutdown;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import service.RequestProcessor;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class AdminController extends Controller {

    private final CoordinatedShutdown coordinatedShutdown;
    private final RequestProcessor requestProcessor;

    @Inject
    public AdminController(CoordinatedShutdown coordinatedShutdown,
                           RequestProcessor requestProcessor){
        this.coordinatedShutdown = coordinatedShutdown;
        this.requestProcessor = requestProcessor;
    }

    public CompletionStage<Result> getAllTasks() {
        return requestProcessor
                .getAllTasks()
                .thenApply(Json::toJson)
                .thenApply(Results::ok);
    }

    public CompletionStage<Result> shutdown(){
        CompletableFuture.runAsync(coordinatedShutdown::run).thenRun(() -> System.exit(0));
        return CompletableFuture.completedFuture(ok());
    }

}
