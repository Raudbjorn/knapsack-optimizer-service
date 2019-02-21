package controllers.knapsack;

import akka.actor.CoordinatedShutdown;
import dto.responses.TaskResponse;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import processors.TaskProcessor;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class AdminController extends Controller {

    private final CoordinatedShutdown coordinatedShutdown;
    private final TaskProcessor taskProcessor;

    @Inject
    public AdminController(CoordinatedShutdown coordinatedShutdown, TaskProcessor taskProcessor){
        this.coordinatedShutdown = coordinatedShutdown;
        this.taskProcessor = taskProcessor;
    }

    public CompletionStage<Result> getAllTasks() {
        return taskProcessor
                .getAllTasks()
                .thenApply(TaskResponse::fromTasks)
                .thenApply(Json::toJson)
                .thenApply(Results::ok);
    }

    public CompletionStage<Result> shutdown(){
        CompletableFuture.runAsync(coordinatedShutdown::run).thenRun(() -> System.exit(0));
        return CompletableFuture.completedFuture(ok());
    }

}
