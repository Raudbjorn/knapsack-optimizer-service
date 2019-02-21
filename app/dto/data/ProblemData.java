package dto.data;

import lombok.Data;
import play.libs.Json;

@Data
public class ProblemData {

    private long capacity;

    private long[] weights;

    private long[] values;


    public static ProblemData fromJson(String json){
        return Json.fromJson(Json.parse(json), ProblemData.class);
    }

    public String toJson(){
        return Json.toJson(this).toString();
    }
}
