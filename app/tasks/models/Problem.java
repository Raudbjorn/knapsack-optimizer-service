package tasks.models;

import lombok.Builder;
import lombok.Value;
import lombok.experimental.Wither;

@Value
@Wither
@Builder
public class Problem {

    private Long id;

    private Long taskId;

    private long capacity;

    private long[] weights;

    private long[] values;
}
