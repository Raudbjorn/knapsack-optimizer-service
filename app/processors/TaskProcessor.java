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


    public CompletionStage<Task> submitProblem(Problem problem){
        Task task = Task.createSubmitted();

        CompletionStage<Integer> eventualTaskId =  db.call(connection -> {
            connection.setAutoCommit(false);
            Integer taskId = TaskRepository.saveTask(task).call(connection);
            ProblemRepository.saveProblem(problem, task.withId(taskId)).call(connection);
            connection.commit();
            return taskId;
        });


        CompletionStage<Task> eventualResult = eventualTaskId.thenApply(task::withId);

        eventualResult.thenAccept(t -> {
            System.out.println("t: " + t);
            CompletableFuture.runAsync(() -> startTask(t), taskContext);
        });

        return eventualResult;
    }


    public CompletionStage<Optional<Task>> getTask(long id){
        return db.call(TaskRepository.getTask(id));
    }

    private void startTask(Task task) {
        CompletionStage<Problem> eventualProblem = db.call(connection -> {
            connection.setAutoCommit(false);
            TaskRepository.updateTaskStatus(task.getId(), STARTED).call(connection);
            Problem problem = ProblemRepository.getProblemByTaskId(task.getId()).call(connection);
            connection.commit();
            return problem;
        });

        eventualProblem.thenAccept(problem -> {
            System.out.println("?2");
            Solution solution = solver.solve(problem);
            System.out.println(solution);
             db.call(connection -> {
                connection.setAutoCommit(false);
                System.out.println(solution);
                SolutionRepository.insertSolution(solution, problem.getId(), task.getId()).call(connection);
                TaskRepository.updateTaskStatus(task.getId(), COMPLETED).call(connection);
                connection.commit();
                return 1;
            });
        });
    }

}
