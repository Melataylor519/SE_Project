import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ComputeEngineImpl implements ComputeEngine {
    @Override
    public String compute(int value) {
        try {
            if (value <= 0) {
                throw new IllegalArgumentException("Input must be a positive integer.");
            }

            BigInteger n = new BigInteger(String.valueOf(value));
            List<BigInteger> factors = new ArrayList<>();
            BigInteger i = BigInteger.TWO;

            while (i.multiply(i).compareTo(n) <= 0) {
                while (n.mod(i).equals(BigInteger.ZERO)) {
                    factors.add(i);
                    n = n.divide(i);
                }
                i = i.add(BigInteger.ONE);
            }

            if (n.compareTo(BigInteger.ONE) > 0) {
                factors.add(n);
            }

            return factors.stream().map(BigInteger::toString).collect(Collectors.joining(","));
        } catch (IllegalArgumentException e) {
            return "ERROR: " + e.getMessage();
        } catch (Exception e) {
            return "ERROR: Unexpected failure - " + e.getMessage();
        }
    }
}

