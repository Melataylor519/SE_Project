package computecomponents;

public class ComputeSystemImpl implements ComputeSystem{

	@Override
	public ComputeResponse compute(ComputeRequest request) {
		return ComputeResponse.SUCCESS;
	}

}
