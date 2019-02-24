package service.validation;

import io.vavr.collection.Seq;
import io.vavr.control.Validation;
import service.json.data.Problem;
import service.json.requests.ProblemRequest;
import service.validation.valid.ValidProblem;

import java.math.BigInteger;
import java.util.List;
import java.util.function.Predicate;

import static service.validation.ServiceError.*;

public class RequestValidation {

    private static final BigInteger MAX_SIZE = BigInteger.valueOf(Long.MAX_VALUE);
    private static final BigInteger MIN_SIZE = BigInteger.valueOf(0L);

    private static final Predicate<BigInteger> GREATER_THAN_MAX = w -> w.max(MAX_SIZE).equals(w);
    private static final Predicate<BigInteger> LOWER_THAN_MIN = w -> w.min(MIN_SIZE).equals(w);


    public static Validation<Seq<ServiceError>, ValidProblem> validateProblemRequest(ProblemRequest problemRequest) {
        Validation<ServiceError, Problem> checkBounds =
                validateListBounds(problemRequest.getProblem());

        Validation<Seq<ServiceError>, ValidProblem> result = Validation.combine(
                validateCapacity(problemRequest.getProblem().getCapacity()),
                validateWeights(problemRequest.getProblem().getWeights()),
                validateValues(problemRequest.getProblem().getValues())
        ).ap(ValidProblem::new);

        return result
                .flatMap(r -> checkBounds.isValid()
                        ? Validation.valid(r)
                        : Validation.invalid(io.vavr.collection.List.of(checkBounds.getError())));
    }

    static Validation<ServiceError, List<BigInteger>> validateWeights(List<BigInteger> weights) {
        return weights.stream().anyMatch(GREATER_THAN_MAX.or(LOWER_THAN_MIN))
                ? Validation.invalid(WEIGHT_OUT_OF_BOUNDS_ERROR)
                : Validation.valid(weights);
    }

    static Validation<ServiceError, List<BigInteger>> validateValues(List<BigInteger> values) {
        return values.stream().anyMatch(GREATER_THAN_MAX.or(LOWER_THAN_MIN))
                ? Validation.invalid(VALUE_OUT_OF_BOUNDS_ERROR)
                : Validation.valid(values);
    }

    static Validation<ServiceError, BigInteger> validateCapacity(BigInteger capacity) {
        return GREATER_THAN_MAX.and(LOWER_THAN_MIN).test(capacity)
                ? Validation.invalid(CAPACITY_OUT_OF_BOUNDS_ERROR)
                : Validation.valid(capacity);
    }


    static Validation<ServiceError, Problem> validateListBounds(Problem problem) {
        return problem.getWeights().size() != problem.getValues().size()
                ? Validation.invalid(WEIGHTS_AND_VALUES_SIZE_MISMATCH)
                : Validation.valid(problem);
    }


}
