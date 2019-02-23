package service.validation;

import static play.mvc.Http.Status.BAD_REQUEST;

public enum ServiceError {
    WEIGHT_OUT_OF_BOUNDS_ERROR(String.format("Weights should be greater than zero and lower than %d", Long.MAX_VALUE), BAD_REQUEST),
    VALUE_OUT_OF_BOUNDS_ERROR(String.format("Values should be greater than zero and lower than %d", Long.MAX_VALUE), BAD_REQUEST),
    WEIGHTS_AND_VALUES_SIZE_MISMATCH("Weights array and values array need to be the same size", BAD_REQUEST);

    public String errorMsg;
    public int status;
    ServiceError(String errorMsg, int status){
        this.errorMsg = errorMsg;
        this.status = status;
    }

}
