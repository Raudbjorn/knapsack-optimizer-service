package service.validation;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.vavr.collection.Seq;
import play.mvc.Result;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import static play.mvc.Http.Status.BAD_REQUEST;
import static play.mvc.Http.Status.INTERNAL_SERVER_ERROR;
import static play.mvc.Results.status;

public enum ServiceError {
    WEIGHT_OUT_OF_BOUNDS_ERROR(String.format("Weights should be greater than zero and lower than %d", Long.MAX_VALUE), BAD_REQUEST),
    VALUE_OUT_OF_BOUNDS_ERROR(String.format("Values should be greater than zero and lower than %d", Long.MAX_VALUE), BAD_REQUEST),
    CAPACITY_OUT_OF_BOUNDS_ERROR(String.format("Values should be greater than zero and lower than %d", Long.MAX_VALUE), BAD_REQUEST),

    WEIGHTS_AND_VALUES_SIZE_MISMATCH("Weights array and values array need to be the same size", BAD_REQUEST);

    public String errorMsg;
    public int status;
    ServiceError(String errorMsg, int status){
        this.errorMsg = errorMsg;
        this.status = status;
    }

    public static CompletionStage<Result> toResult(Seq<ServiceError> errors){
        int httpResponseCode =
                errors.toJavaStream().mapToInt(e -> e.status).max().orElse(INTERNAL_SERVER_ERROR);
        List<String> errorMsgs = errors.toJavaStream()
                .map(e -> e.errorMsg)
                .collect(Collectors.toList());

        ObjectNode node = JsonNodeFactory.instance.objectNode();
        ArrayNode arrayNode = node.putArray("errors");
        errorMsgs.forEach(arrayNode::add);

        return CompletableFuture.completedFuture(status(httpResponseCode, node));
    }


}
