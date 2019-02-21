package models.knapsack;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import play.libs.Json;

@Data
public class Problem {

    @JsonIgnore
    private Integer Id;

    private long capacity;

    private long[] weights;

    private long[] values;

    public static Problem fromJson(String json){
        return Json.fromJson(Json.parse(json), Problem.class);
    }


    public String toJson(){
        return Json.toJson(this).toString();
    }


}
