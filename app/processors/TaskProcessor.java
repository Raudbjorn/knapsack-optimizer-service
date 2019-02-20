package processors;

import contexts.TaskExecutionContext;
import models.knapsack.Problem;
import models.knapsack.Solution;
import models.knapsack.Task;
import repositories.ProblemRepository;
import repositories.SolutionRepository;
import repositories.TaskRepository;
import repositories.db.Db;
import solvers.ServiceKnapsackSolver;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static models.knapsack.Task.TaskStatus.COMPLETED;
import static models.knapsack.Task.TaskStatus.STARTED;

@Singleton
public class TaskProcessor {

    private final Db db;
    private final TaskExecutionContext taskContext;
    private final ServiceKnapsackSolver solver;

    @Inject
    public TaskProcessor(Db db, TaskExecutionContext context, ServiceKnapsackSolver solver) {
        this.db = db;
        this.taskContext = context;
        this.solver = solver;
    }


    public CompletionStage<Task> submitProblem(Problem problem) {
        Task task = Task.createSubmitted();

        CompletionStage<Task> eventualResult = db.call(connection -> {
            connection.setAutoCommit(false);
            Integer taskId = TaskRepository.saveTask(task).call(connection);
            ProblemRepository.saveProblem(problem, task.withId(taskId)).call(connection);
            Task result = TaskRepository
                    .getTask(taskId)
                    .call(connection)
                    .orElseThrow(() -> new RuntimeException(String.format("Could not retrieve persisted task with id %d", taskId)));
            connection.commit();
            return result;
        });

        eventualResult.thenAcceptAsync(t -> CompletableFuture.runAsync(() -> startTask(t), taskContext));

        return eventualResult;
    }

    public CompletionStage<Optional<Solution>> getSolution(int taskId){
        return db.call(SolutionRepository.getSolutionByTaskId(taskId));
    }

    public CompletionStage<Optional<Task>> getTask(long id) {
        return db.call(TaskRepository.getTask(id));
    }

    public CompletionStage<List<Task>> getAllTasks(){
        return db.call(TaskRepository.getAll());
    }

    private Optional<Solution> checkIfSolutionExists(Problem problem){
        //TODO: IMPLEMENT
        return null;
    }


    private void startTask(Task task) {
        CompletionStage<Problem> eventualProblem = db.call(connection -> {
            connection.setAutoCommit(false);
            TaskRepository.updateTaskStatus(task.getId(), STARTED).call(connection);
            Problem problem = ProblemRepository.getProblemByTaskId(task.getId()).call(connection);
            connection.commit();
            return problem;
        }, taskContext);

        eventualProblem
                .thenApply(solver::solve)
                .thenAcceptAsync(solution -> db.run(connection -> {
                    connection.setAutoCommit(false);
                    TaskRepository.updateTaskStatus(task.getId(), COMPLETED).call(connection);
                    SolutionRepository.insertSolution(solution, task.getId()).call(connection);
                    connection.commit();
                }, taskContext));
    }

}
