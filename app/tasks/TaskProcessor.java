package tasks;

import database.Db;
import database.repositories.ProblemRepository;
import database.repositories.SolutionRepository;
import database.repositories.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tasks.context.TaskExecutionContext;
import tasks.models.Problem;
import tasks.models.Solution;
import tasks.models.Task;
import tasks.solvers.ServiceKnapsackSolver;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static tasks.models.Task.TaskStatus.COMPLETED;
import static tasks.models.Task.TaskStatus.STARTED;

@Singleton
public class TaskProcessor {

    private final Db db;
    private final TaskExecutionContext taskContext;

    private final Logger logger = LoggerFactory.getLogger("play");

    @Inject
    public TaskProcessor(Db db, TaskExecutionContext context) {
        this.db = db;
        this.taskContext = context;
    }

    public CompletionStage<Task> submitProblem(service.json.data.Problem problem){
        //TODO: IMPLEMENT LIKE BELOW!
        return null;
    }

/*
    public CompletionStage<Task> submitProblem(ProblemData problem) {
        CompletionStage<Optional<Task>> maybeSolution = checkIfSolutionExists(problem);
        return maybeSolution.thenCompose(maybeSolvedTask ->
                maybeSolvedTask.map(CompletableFuture::completedFuture).orElseGet(() -> {
                    return createAndStart(problem).toCompletableFuture();
                })
        );
    }
    */

    public CompletionStage<Optional<Solution>> getSolution(int taskId) {
        return db.call(SolutionRepository.getSolutionByTaskId(taskId));
    }

    public CompletionStage<Problem> getProblemByTaskId(int taskId){
        return db.call(ProblemRepository.getProblemByTaskId(taskId));
    }

    public CompletionStage<Optional<Task>> getTask(long id) {
        return db.call(TaskRepository.getTask(id));
    }

    public CompletionStage<List<Task>> getAllTasks() {
        return db.call(TaskRepository.getAll());
    }

    private CompletionStage<Optional<Task>> checkIfSolutionExists(ProblemData problem) {
        return db.call(TaskRepository.checkForSoluiton(problem));
    }

    private CompletionStage<Task> createSubmittedTask(ProblemData problem) {
        return db.call(connection -> {
            Task task = Task.createSubmitted();
            connection.setAutoCommit(false);
            Long taskId = TaskRepository.saveTask(task).call(connection);
            ProblemRepository.saveProblem(problem, task.withId(taskId)).call(connection);
            Task result = TaskRepository
                    .getTask(taskId)
                    .call(connection)
                    .orElseThrow(() -> new RuntimeException(String.format("Could not retrieve persisted task with id %d", taskId)));
            connection.commit();
            return result;
        });
    }


    private CompletionStage<Task> createAndStart(ProblemData problem) {
        CompletionStage<Task> taskSubmitted = createSubmittedTask(problem);

        taskSubmitted.thenAcceptAsync(submittedTask -> {
            startTask(submittedTask).thenApply(ServiceKnapsackSolver::solve).thenApply(this::completeTask);
        });

        return taskSubmitted;
    }

    private CompletionStage<Problem> startTask(Task task) {
        return db.call(connection -> {
            connection.setAutoCommit(false);
            TaskRepository.updateTaskStatus(task.getId(), STARTED).call(connection);
            Problem problem = ProblemRepository.getProblemByTaskId(task.getId()).call(connection);
            connection.commit();
            return problem.withTaskId(task.getId());
        }, taskContext);
    }

    private CompletionStage<Boolean> completeTask(Solution solution) {
        logger.info("3");
        return db.call(connection -> {
            logger.info("???1");
            connection.setAutoCommit(false);
            TaskRepository.updateTaskStatus(solution.getTaskId(), COMPLETED).call(connection);
            SolutionRepository.insertSolution(solution, solution.getTaskId()).call(connection);
            connection.commit();
            logger.info("???2");
            return true;
        }, taskContext);
    }

}
