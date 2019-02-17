package models.knapsack;

import lombok.Data;

@Data
public class Solution implements ServiceModel{

    private int[] items;

    private long time;
}
