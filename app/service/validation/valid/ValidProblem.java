package service.validation.valid;

import service.json.data.Problem;

import java.math.BigInteger;
import java.util.List;

public class ValidProblem extends Problem {

    public ValidProblem(BigInteger capacity,
                        List<BigInteger> weights,
                        List<BigInteger> values) {
        super(capacity, weights, values);
    }

}
