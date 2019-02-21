package solvers;

import dto.data.SolutionData;
import models.knapsack.Problem;
import models.knapsack.Solution;

import java.util.stream.IntStream;

public class ServiceKnapsackSolver {




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


    public static Solution solve(Problem problem) {
        long[][] solverWeights = new long[][]{problem.getProblemData().getWeights()};
        long[] solverCapacity = new long[]{problem.getProblemData().getCapacity()};

        com.google.ortools.algorithms.KnapsackSolver solver =
                new com.google.ortools.algorithms.KnapsackSolver(
                        com.google.ortools.algorithms.KnapsackSolver.SolverType.KNAPSACK_DYNAMIC_PROGRAMMING_SOLVER,
                        "test");
        solver.init(problem.getProblemData().getValues(), solverWeights, solverCapacity);
        solver.solve();
        int[] packedItems = IntStream.range(0, solverWeights[0].length)
                .filter(solver::bestSolutionContains)
                .toArray();

        SolutionData data = new SolutionData();
        data.setItems(packedItems);

        return Solution.builder()
                .solutionData(data)
                .problemId(problem.getId())
                .taskId(problem.getTaskId())
                .build();
    }
}
