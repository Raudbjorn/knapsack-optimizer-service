package dto.data;

import lombok.Data;
import play.libs.Json;

@Data
public class SolutionData {

    private int[] items;

    private Integer time;

    public static SolutionData fromJson(String json){
        return Json.fromJson(Json.parse(json), SolutionData.class);
    }

}
