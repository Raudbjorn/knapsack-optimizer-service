package service.json.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Problem {
    BigInteger capacity;
    List<BigInteger> weights;
    List<BigInteger> values;
}
