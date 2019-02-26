package tasks.solvers;

import org.junit.Test;
import tasks.models.Problem;
import tasks.models.Solution;

import java.nio.file.Paths;
import java.util.Arrays;

import static org.junit.Assert.*;

public class ServiceKnapsackSolverTest {

    static {
        System.load(Paths.get("./lib/libjniortools.so").toAbsolutePath().toString());
    }

    @Test
    public void solve() {
        long capacity = 60;
        long[] weights = new long[]{10, 20, 33};
        long[] values = new long[]{10, 3, 30};

        Problem testProblem = Problem
                .builder()
                .capacity(capacity)
                .weights(weights)
                .values(values)
                .build();

        Solution solution = new ServiceKnapsackSolver().solve(testProblem);
        assertArrayEquals(solution.getItems(), new int[]{0, 2});
    }
}