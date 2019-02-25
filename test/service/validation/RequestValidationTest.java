package service.validation;

import io.vavr.control.Validation;
import org.junit.Test;
import service.json.data.Problem;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static org.junit.Assert.*;

public class RequestValidationTest {

    @Test
    public void validateWeights() {
        List<BigInteger> okWeights = LongStream.of(1,2,3).mapToObj(BigInteger::valueOf).collect(Collectors.toList());

        Validation<ServiceError, List<BigInteger>> testValidation = RequestValidation.validateWeights(okWeights);
        assertTrue(testValidation.isValid());

        List<BigInteger> notOkWeights = Arrays.asList(BigInteger.valueOf(-1L));
        testValidation = RequestValidation.validateWeights(notOkWeights);
        assertFalse(testValidation.isValid());
        assertEquals(testValidation.getError(), ServiceError.WEIGHT_OUT_OF_BOUNDS_ERROR);

        notOkWeights = Arrays.asList(BigInteger.valueOf(Long.MAX_VALUE).add(BigInteger.ONE));
        testValidation = RequestValidation.validateWeights(notOkWeights);
        assertFalse(testValidation.isValid());
        assertEquals(testValidation.getError(), ServiceError.WEIGHT_OUT_OF_BOUNDS_ERROR);
    }

    @Test
    public void validateValues() {
        List<BigInteger> okValues = LongStream.of(1,2,3).mapToObj(BigInteger::valueOf).collect(Collectors.toList());

        Validation<ServiceError, List<BigInteger>> testValidation = RequestValidation.validateValues(okValues);
        assertTrue(testValidation.isValid());

        List<BigInteger> notOkValues = Arrays.asList(BigInteger.valueOf(-1L));
        testValidation = RequestValidation.validateValues(notOkValues);
        assertFalse(testValidation.isValid());
        assertEquals(testValidation.getError(), ServiceError.VALUE_OUT_OF_BOUNDS_ERROR);

        notOkValues = Arrays.asList(BigInteger.valueOf(Long.MAX_VALUE).add(BigInteger.ONE));
        testValidation = RequestValidation.validateValues(notOkValues);
        assertFalse(testValidation.isValid());
        assertEquals(testValidation.getError(), ServiceError.VALUE_OUT_OF_BOUNDS_ERROR);
    }

    @Test
    public void validateCapacity() {
        BigInteger okCapacity = BigInteger.TEN;
        Validation<ServiceError, BigInteger> capacityValidation = RequestValidation.validateCapacity(okCapacity);

        assertTrue(capacityValidation.isValid());

        BigInteger notOkCapacity = BigInteger.valueOf(Long.MAX_VALUE).add(BigInteger.ONE);
        capacityValidation = RequestValidation.validateCapacity(notOkCapacity);
        assertFalse(capacityValidation.isValid());
        assertEquals(ServiceError.CAPACITY_OUT_OF_BOUNDS_ERROR, capacityValidation.getError());

        notOkCapacity = BigInteger.valueOf(-1);
        capacityValidation = RequestValidation.validateCapacity(notOkCapacity);
        assertFalse(capacityValidation.isValid());
        assertEquals(ServiceError.CAPACITY_OUT_OF_BOUNDS_ERROR, capacityValidation.getError());
    }

    @Test
    public void validateListBounds() {
        List<BigInteger> weights = Arrays.asList(BigInteger.ONE, BigInteger.TEN, BigInteger.ONE);
        List<BigInteger> values = Arrays.asList(BigInteger.TEN, BigInteger.ONE, BigInteger.TEN);

        Problem problem = new Problem();
        problem.setWeights(weights);
        problem.setValues(values);
        Validation<ServiceError, Problem> testValidation = RequestValidation.validateListBounds(problem);
        assertTrue(testValidation.isValid());

        List<BigInteger> notOkWeights = weights.subList(0, 2);
        problem.setWeights(notOkWeights);
        testValidation = RequestValidation.validateListBounds(problem);
        assertFalse(testValidation.isValid());
        assertEquals(ServiceError.WEIGHTS_AND_VALUES_SIZE_MISMATCH, testValidation.getError());
    }
}