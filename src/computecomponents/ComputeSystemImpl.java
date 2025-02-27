package computecomponents;

public class ComputeSystemImpl implements ComputeSystem{

	@Override
	public ComputeResponse compute(ComputeRequest request) {
		// get input data from InputConfig (String)
        String inputData = request.getInputConfig().getInputData();

        // check input data
        if (inputData == null || inputData.isEmpty()) {
            return new ComputeResponseImpl(ComputeResponse.ComputeResponseStatus.INVALID_REQUEST, "Invalid input data");
        }

        // Largest Prime Factor compute
        String result = calculateLargestPrimeFactors(inputData);

        // return result
        return new ComputeResponseImpl(ComputeResponse.ComputeResponseStatus.SUCCESS, result);
    }
	
    private String calculateLargestPrimeFactors(String inputData) {
        // parse input data to long data type
        long number = Long.parseLong(inputData.trim());
        StringBuilder result = new StringBuilder();

        // compute prime factor
        for (long i = 2; i <= number; i++) {
            while (number % i == 0) {
                result.append(i).append(" "); 
                number /= i;
            }
        }

        return result.toString().trim();  // return result as space-separated String
    }

}
