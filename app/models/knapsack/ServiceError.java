package models.knapsack;

import static play.mvc.Http.Status.BAD_REQUEST;

public enum ServiceError {
    WEIGHT_OUT_OF_BOUNDS_ERROR(String.format("Weights should be greater than zero and lower than %d", Integer.MAX_VALUE), BAD_REQUEST);

    public String errorMsg;
    public int status;
    ServiceError(String errorMsg, int status){
        this.errorMsg = errorMsg;
        this.status = status;
    }

}
