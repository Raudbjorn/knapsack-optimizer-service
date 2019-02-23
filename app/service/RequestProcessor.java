package service;

import database.Db;
import database.repositories.SolutionRepository;
import database.repositories.TaskRepository;
import database.util.ResultSetProvider;
import database.util.ResultSetReadable;
import service.json.data.Problem;
import service.json.responses.SolutionResponse;
import service.json.responses.TaskResponse;
import service.json.responses.TasksResponse;
import tasks.TaskProcessor;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static database.util.ResultSetReadable.maybe;

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
        return db.call(TaskRepository.getAllTasks(TasksResponse.READ_RESULT_SET));
    }


    public CompletionStage<Optional<SolutionResponse>> getSolution(long taskId) {
        ResultSetProvider<Optional<SolutionResponse>> query =
                SolutionRepository.getSolutionResponseDataByTaskId(taskId);

        ResultSetReadable<Optional<SolutionResponse>> reader =
                maybe(SolutionResponse.READ_RESULT_SET);

        return db.call(query.withReader(reader));
    }

    public CompletionStage<TaskResponse> createTask(Problem problem){
        return taskProcessor.submitProblem(problem).thenApply(TaskResponse::fromTask);
    }

    public CompletionStage<Optional<TaskResponse>>getTask(long taskId) {
        ResultSetProvider<Optional<TaskResponse>> query = TaskRepository.getTask(taskId);
        ResultSetReadable<Optional<TaskResponse>> reader = maybe(TaskResponse.READ_RESULT_SET);
        return db.call(query.withReader(reader));
    }



}
