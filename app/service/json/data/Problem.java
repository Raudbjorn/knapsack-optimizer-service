package service.json.data;

import lombok.Data;

import java.math.BigInteger;
import java.util.List;

@Data
public class Problem {
    BigInteger capacity;
    List<BigInteger> weights;
    List<BigInteger> values;
}
