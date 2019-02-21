package models.knapsack;

import lombok.Data;
import play.libs.Json;

@Data
public class Solution {

    private int[] items;

    private long time;

    public static Solution fromJson(String json){
        return Json.fromJson(Json.parse(json), Solution.class);
    }

}
