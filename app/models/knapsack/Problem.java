package models.knapsack;

import lombok.Data;

@Data
public class Problem {

    private long capacity;

    private long[] weights;

    private long[] values;

}
