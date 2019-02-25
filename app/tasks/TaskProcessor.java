package tasks;

import database.Db;
import database.repositories.ProblemRepository;
import database.repositories.SolutionRepository;
import database.repositories.TaskRepository;
import database.util.ResultSetProvider;
import tasks.context.TaskExecutionContext;
import tasks.models.Problem;
import tasks.models.Solution;
import tasks.models.Task;
import tasks.solvers.ServiceKnapsackSolver;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ConcurrentHashMap;

import static database.util.ResultSetReadable.maybe;
import static tasks.models.Task.TaskStatus.COMPLETED;
import static tasks.models.Task.TaskStatus.STARTED;

@Singleton
public class TaskProcessor {

    private final Db db;
    private final TaskExecutionContext taskContext;
    private final ServiceKnapsackSolver solver;

    private static final Map<Long, CompletionStage<Void>> RUNNING_TASKS = new ConcurrentHashMap();

    @Inject
    public TaskProcessor(Db db, TaskExecutionContext context, ServiceKnapsackSolver solver) {
        this.db = db;
        this.taskContext = context;
        this.solver = solver;
    }

    public CompletionStage<Task> submitProblem(Problem problem) {
        CompletionStage<Optional<Task>> maybeSolution = checkIfSolutionExists(problem);

        return maybeSolution.thenCompose(maybeSolvedTask ->
                maybeSolvedTask.map(CompletableFuture::completedFuture).orElseGet(() -> {
                    return createAndStart(problem).toCompletableFuture();
                })
        );
    }

    private CompletionStage<Optional<Task>> checkIfSolutionExists(Problem problem) {
        return db.call(TaskRepository.checkForSolution(problem));
    }

    private CompletionStage<Task> createSubmittedTask(Problem problem) {
        return db.call(connection -> {
            Task task = Task.createSubmitted();
            connection.setAutoCommit(false);
            Long taskId = TaskRepository.insertTask(task).call(connection);
            ProblemRepository.saveProblem(problem, taskId).call(connection);
            ResultSetProvider<Optional<Task>> taskQuery = TaskRepository.getTask(taskId);
            Task result = taskQuery
                    .withReader(maybe(Task.READ_RESULT_SET))
                    .call(connection)
                    .orElseThrow(() -> new RuntimeException(String.format("Could not retrieve persisted task with id %d", taskId)));
            connection.commit();
            return result;
        });
    }

    private CompletionStage<Task> createAndStart(Problem problem) {
        CompletionStage<Task> taskSubmitted = createSubmittedTask(problem);

        taskSubmitted.thenAccept(t -> {
            CompletionStage<Void> runningTask = CompletableFuture.runAsync(() ->
                            startTask(t)
                                    .thenApply(solver::solve)
                                    .thenApply(this::completeTask),
                    taskContext);
            RUNNING_TASKS.put(t.getId(), runningTask);
        });

        return taskSubmitted;
    }

    private CompletionStage<Problem> startTask(Task task) {
        return db.call(connection -> {
            connection.setAutoCommit(false);
            TaskRepository.updateTaskStatus(task.getId(), STARTED.name()).call(connection);
            Problem problem = ProblemRepository.getProblemByTaskId(task.getId()).call(connection)
                    .orElseThrow(() -> new RuntimeException(String.format("Could not find problem for task: %s", task)));
            connection.commit();
            return problem.withTaskId(task.getId());
        }, taskContext);
    }

    private CompletionStage<Boolean> completeTask(Solution solution) {
        return db.call(connection -> {
            connection.setAutoCommit(false);
            TaskRepository.updateTaskStatus(solution.getTaskId(), COMPLETED.name()).call(connection);
            SolutionRepository.insertSolution(solution).call(connection);
            RUNNING_TASKS.remove(solution.getTaskId());
            connection.commit();
            return true;
        }, taskContext);
    }

    public boolean cancelTask(long taskId){
        CompletionStage<Void> task = RUNNING_TASKS.get(taskId);
        if(null == task){
            return false;
        } else {
            task.toCompletableFuture().cancel(true);
            RUNNING_TASKS.remove(taskId);
            return true;
        }
    }

}
