public class ComputationCoordinatorPrototype {
	public void prototype(ComputationCoordinator coordinator) {
        try {
            ComputeRequest request = new ComputeRequest(
                new BasicInputConfig(15), new BasicOutputConfig("testUser"), ','
            );
            ComputeResult result = coordinator.compute(request);

            if (result.getStatus().isSuccess()) {
                System.out.println("Coordinator call successful.");
            } else {
                System.err.println("Coordinator call failed: " + result.getFailureMessage());
            }
        } catch (Exception e) {
            System.err.println("Exception during coordinator prototype: " + e.getMessage());
        }
    }
}