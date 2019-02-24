package tasks.models;

import lombok.Builder;
import lombok.Value;
import lombok.experimental.Wither;

@Value
@Builder
@Wither
public class Solution {

    private Long id;

    private Long problemId;

    private Long taskId;

    private int[] items;

}
