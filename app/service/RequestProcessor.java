package service;

import database.Db;
import database.repositories.SolutionRepository;
import database.repositories.TaskRepository;
import database.util.ResultSetProvider;
import database.util.ResultSetReadable;
import service.json.responses.SolutionResponse;
import service.json.responses.TaskResponse;
import service.json.responses.TasksResponse;
import service.validation.valid.ValidProblem;
import tasks.TaskProcessor;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static database.util.ResultSetReadable.maybe;
import static tasks.models.Problem.fromJson;

@Singleton
public class RequestProcessor {

    private final TaskProcessor taskProcessor;
    private final Db db;

    @Inject
    public RequestProcessor(TaskProcessor taskProcessor, Db db){
        this.taskProcessor = taskProcessor;
        this.db = db;
    }

    public CompletionStage<TasksResponse> getAllTasks(){
        ResultSetProvider<TasksResponse> query = TaskRepository.getAllTasks();
        return db.call(query.withReader(TasksResponse.READ_RESULT_SET));
    }

    public CompletionStage<Optional<SolutionResponse>> getSolution(long taskId) {
        ResultSetProvider<Optional<SolutionResponse>> query =
                SolutionRepository.getSolutionResponseDataByTaskId(taskId);

        ResultSetReadable<Optional<SolutionResponse>> reader =
                maybe(SolutionResponse.READ_RESULT_SET);

        return db.call(query.withReader(reader));
    }

    public CompletionStage<TaskResponse> createTask(ValidProblem problem){
        return taskProcessor.submitProblem(fromJson(problem)).thenApply(TaskResponse::fromTask);
    }

    public CompletionStage<Optional<TaskResponse>>getTask(long taskId) {
        ResultSetProvider<Optional<TaskResponse>> query = TaskRepository.getTask(taskId);
        ResultSetReadable<Optional<TaskResponse>> reader = maybe(TaskResponse.READ_RESULT_SET);
        return db.call(query.withReader(reader));
    }

    public boolean cancelTask(long taskId){
        return taskProcessor.cancelTask(taskId);
    }

}
