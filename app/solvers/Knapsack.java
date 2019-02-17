package solvers;

import com.google.common.base.Stopwatch;
import com.google.ortools.algorithms.KnapsackSolver;
import io.vavr.control.Either;
import models.knapsack.Problem;
import models.knapsack.ServiceError;
import models.knapsack.Solution;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;


import static models.knapsack.ServiceError.WEIGHT_OUT_OF_BOUNDS_ERROR;

public class Knapsack {

    static {
        System.loadLibrary("jniortools");
    }

    public static Either<ServiceError, Solution> solve(Problem problem) {
        if (Arrays.stream(problem.getWeights()).anyMatch(w -> w > Integer.MAX_VALUE || w <= 0))
            return Either.left(WEIGHT_OUT_OF_BOUNDS_ERROR);

        long[][] solverWeights = new long[][]{problem.getWeights()};
        long[] solverCapacity = new long[]{problem.getCapacity()};

        KnapsackSolver solver = new KnapsackSolver(KnapsackSolver.SolverType.KNAPSACK_MULTIDIMENSION_BRANCH_AND_BOUND_SOLVER, "test");
        solver.init(problem.getValues(), solverWeights, solverCapacity);

        Stopwatch stopwatch = Stopwatch.createUnstarted();
        solver.solve();
        long time = stopwatch.elapsed(TimeUnit.MILLISECONDS);

        int[] packedItems = Arrays.stream(solverWeights[0])
                .mapToInt(w -> (int) w)
                .filter(solver::bestSolutionContains)
                .toArray();

        long[] packedWeights =
                Arrays.stream(packedItems).mapToLong(item -> solverWeights[0][item]).toArray();

        Solution result = new Solution();
        result.setItems(packedItems);
        result.setTime(time);

        return Either.right(result);
    }
}
