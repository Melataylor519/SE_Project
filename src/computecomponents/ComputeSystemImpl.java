// ComputeSystemImpl.java

package computecomponents;

import main.java.com.assignment2.api.UserComputeEngineAPI;
import project.annotations.DataProcessingAPI;

public class ComputeSystemImpl implements ComputeSystem{
	
	private DataProcessingAPI dp;
	private UserComputeEngineAPI uce;

	public ComputeSystemImpl(DataProcessingAPI dp, UserComputeEngineAPI uce) {
		this.dp = dp;
		this.uce = uce;
	}

	@Override
	public ComputeResponse compute(ComputeRequest request) {
		try {
            // get input data from InputConfig (String)
            String inputData = request.getInputConfig().getInputData();

            // validate input data
            if (inputData == null || inputData.trim().isEmpty()) {
                return new ComputeResponseImpl(ComputeResponse.ComputeResponseStatus.INVALID_REQUEST, "Error: Input data is null or empty.");
            }

            if (!inputData.matches("\\d+")) {  // Ensure input contains only digits
                return new ComputeResponseImpl(ComputeResponse.ComputeResponseStatus.INVALID_REQUEST, "Error: Input data is not a number.");
            }

            // Convert input string to long safely
            long num;
            try {
                num = Long.parseLong(inputData);
            } catch (NumberFormatException e) {
                return new ComputeResponseImpl(ComputeResponse.ComputeResponseStatus.INVALID_REQUEST, "Error: Input number is too large.");
            }

            // compute Largest Prime Factor
            String result = calculateLargestPrimeFactors(num);

            // return result
            return new ComputeResponseImpl(ComputeResponse.ComputeResponseStatus.SUCCESS, result);

        } catch (Exception e) {
            // Catch any unexpected exceptions
            return new ComputeResponseImpl(ComputeResponse.ComputeResponseStatus.FAILURE, "Unexpected error occurred: " + e.getMessage());
        }
    }

    private String calculateLargestPrimeFactors(long num) {
        StringBuilder result = new StringBuilder();

        // Factor out 2s
        while (num % 2 == 0) {
            result.append(2).append(" ");
            num /= 2;
        }

        // Check odd factors
        for (long i = 3; i * i <= num; i += 2) {
            while (num % i == 0) {
                result.append(i).append(" ");
                num /= i;
            }
        }

        // If num is still greater than 2, it is prime
        if (num > 2) {
            result.append(num);
        }

        return result.toString().trim();  // return result as space-separated String
    }
}
