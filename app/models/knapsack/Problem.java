package models.knapsack;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class Problem {

    @JsonIgnore
    private Integer Id;

    private long capacity;

    private long[] weights;

    private long[] values;

}
