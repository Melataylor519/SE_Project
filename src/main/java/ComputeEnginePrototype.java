public class ComputeEnginePrototype {
    public void prototype(ComputeEngine engine) {
        try {
            String result = engine.compute(1);
            System.out.println("Computed result: " + result);
        } catch (Exception e) {
            System.err.println("ERROR in prototype: " + e.getMessage());
        }
    }
}