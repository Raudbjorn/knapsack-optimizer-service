package models.knapsack;

import lombok.Data;

@Data
public class Problem implements ServiceModel {

    private long capacity;

    private long[] weights;

    private long[] values;

}
