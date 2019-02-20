package solvers;

import com.google.common.base.Stopwatch;
import com.google.ortools.algorithms.KnapsackSolver;
import io.vavr.control.Either;
import models.knapsack.Problem;
import models.knapsack.ServiceError;
import models.knapsack.Solution;
import play.libs.NativeLoader;

import javax.inject.Singleton;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;


import static models.knapsack.ServiceError.WEIGHT_OUT_OF_BOUNDS_ERROR;

@Singleton
public class ServiceKnapsackSolver {

    static {
        NativeLoader.load("jniortools");
    }
/*
    public static Either<ServiceError, Solution> solve(Problem problem) {
        if (Arrays.stream(problem.getWeights()).anyMatch(w -> w > Integer.MAX_VALUE || w <= 0))
            return Either.left(WEIGHT_OUT_OF_BOUNDS_ERROR);

        long[][] solverWeights = new long[][]{problem.getWeights()};
        long[] solverCapacity = new long[]{problem.getCapacity()};

        KnapsackSolver solver = new KnapsackSolver(KnapsackSolver.SolverType.KNAPSACK_DYNAMIC_PROGRAMMING_SOLVER, "test");
        solver.init(problem.getValues(), solverWeights, solverCapacity);

        Stopwatch stopwatch = Stopwatch.createUnstarted();
        solver.solve();
        long time = stopwatch.elapsed(TimeUnit.MILLISECONDS);

        int[] packedItems = IntStream.range(0, solverWeights[0].length)
                .filter(solver::bestSolutionContains)
                .toArray();

        Solution result = new Solution();
        result.setItems(packedItems);
        result.setTime(time);

        return Either.right(result);
    }
    */


    public Solution solve(Problem problem) {
        long[][] solverWeights = new long[][]{problem.getWeights()};
        long[] solverCapacity = new long[]{problem.getCapacity()};

        com.google.ortools.algorithms.KnapsackSolver solver = new com.google.ortools.algorithms.KnapsackSolver(com.google.ortools.algorithms.KnapsackSolver.SolverType.KNAPSACK_DYNAMIC_PROGRAMMING_SOLVER, "test");
        solver.init(problem.getValues(), solverWeights, solverCapacity);

        Stopwatch stopwatch = Stopwatch.createUnstarted();
        solver.solve();
        long time = stopwatch.elapsed(TimeUnit.MILLISECONDS);

        int[] packedItems = IntStream.range(0, solverWeights[0].length)
                .filter(solver::bestSolutionContains)
                .toArray();

        Solution result = new Solution();
        result.setItems(packedItems);
        result.setTime(time);

        return result;
    }
}
