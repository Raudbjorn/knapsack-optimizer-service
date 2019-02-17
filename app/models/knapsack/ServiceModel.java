package models.knapsack;

import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;

public interface ServiceModel {

    public default JsonNode toJson(){
        return Json.toJson(this);
    }


}
