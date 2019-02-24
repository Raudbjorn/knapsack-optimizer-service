package tasks.solvers;

import com.google.ortools.algorithms.KnapsackSolver;
import tasks.models.Problem;
import tasks.models.Solution;

import javax.inject.Singleton;
import java.util.stream.IntStream;

@Singleton
public class ServiceKnapsackSolver {

    public Solution solve(Problem problem) {
        long[][] solverWeights = new long[][]{problem.getWeights()};
        long[] solverCapacity = new long[]{problem.getCapacity()};

        com.google.ortools.algorithms.KnapsackSolver solver =
                new com.google.ortools.algorithms.KnapsackSolver(
                        KnapsackSolver.SolverType.KNAPSACK_BRUTE_FORCE_SOLVER,
                        "test");
        solver.init(problem.getValues(), solverWeights, solverCapacity);
        solver.solve();
        int[] packedItems = IntStream.range(0, solverWeights[0].length)
                .filter(solver::bestSolutionContains)
                .toArray();

        return Solution.builder()
                .items(packedItems)
                .problemId(problem.getId())
                .taskId(problem.getTaskId())
                .build();
    }
}
