import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ScriptExecutionSolverTest {

    public static void main(String[] args) {
        ScriptExecutionSolverTest tester = new ScriptExecutionSolverTest();
        tester.runTests();
    }

    public void runTests() {
        testNormalExecutionPlan();
        testMissingScript();
        testShuffledInput();
        testCircularDependency();
    }

    // Test 1: Normal execution plan
    public void testNormalExecutionPlan() {
        System.out.println("Running testNormalExecutionPlan...");
        try {
            VulnerabilityScript script1 = new VulnerabilityScript(1, Arrays.asList(2, 3));
            VulnerabilityScript script2 = new VulnerabilityScript(2, Arrays.asList(3));
            VulnerabilityScript script3 = new VulnerabilityScript(3, Collections.emptyList());
            VulnerabilityScript script4 = new VulnerabilityScript(4, Arrays.asList(1, 2));

            List<VulnerabilityScript> scripts = Arrays.asList(script1, script2, script3, script4);

            List<Integer> expectedPlan = Arrays.asList(3, 2, 1, 4);

            List<Integer> executionPlan = ScriptExecutionSolver.createExecutionPlan(scripts);

            if (executionPlan.equals(expectedPlan)) {
                System.out.println("testNormalExecutionPlan passed.");
            } else {
                System.err.println("testNormalExecutionPlan failed.");
                System.err.println("Expected: " + expectedPlan);
                System.err.println("Got: " + executionPlan);
            }
        } catch (Exception e) {
            System.err.println("testNormalExecutionPlan failed with exception: " + e.getMessage());
        }
    }

    // Test 2: Circular dependency detection
    public void testCircularDependency() {
        System.out.println("\nRunning testCircularDependency...");
        try {
            VulnerabilityScript script1 = new VulnerabilityScript(1, Arrays.asList(2));
            VulnerabilityScript script2 = new VulnerabilityScript(2, Arrays.asList(3));
            VulnerabilityScript script3 = new VulnerabilityScript(3, Arrays.asList(1)); // Circular dependency

            List<VulnerabilityScript> scripts = Arrays.asList(script1, script2, script3);

            ScriptExecutionSolver.createExecutionPlan(scripts);

            System.err.println("testCircularDependency failed. Expected an exception due to circular dependency.");
        } catch (RuntimeException e) {
            if (e.getMessage().contains("Circular dependency detected")) {
                System.out.println("testCircularDependency passed.");
            } else {
                System.err.println("testCircularDependency failed with unexpected exception: " + e.getMessage());
            }
        } catch (Exception e) {
            System.err.println("testCircularDependency failed with unexpected exception: " + e.getMessage());
        }
    }

    // Test 3: Missing script detection
    public void testMissingScript() {
        System.out.println("\nRunning testMissingScript...");
        try {
            VulnerabilityScript script1 = new VulnerabilityScript(1, Arrays.asList(2));
            VulnerabilityScript script2 = new VulnerabilityScript(2, Arrays.asList(99)); // Dependency on non-existent script

            List<VulnerabilityScript> scripts = Arrays.asList(script1, script2);

            ScriptExecutionSolver.createExecutionPlan(scripts);

            System.err.println("testMissingScript failed. Expected an exception due to missing script.");
        } catch (RuntimeException e) {
            if (e.getMessage().contains("Script 99 not found")) {
                System.out.println("testMissingScript passed.");
            } else {
                System.err.println("testMissingScript failed with unexpected exception: " + e.getMessage());
            }
        } catch (Exception e) {
            System.err.println("testMissingScript failed with unexpected exception: " + e.getMessage());
        }
    }

    // Test 4: Shuffled input to ensure consistent output
    public void testShuffledInput() {
        System.out.println("\nRunning testShuffledInput...");
        try {
            VulnerabilityScript script1 = new VulnerabilityScript(1, Arrays.asList(2, 3));
            VulnerabilityScript script2 = new VulnerabilityScript(2, Arrays.asList(3));
            VulnerabilityScript script3 = new VulnerabilityScript(3, Collections.emptyList());
            VulnerabilityScript script4 = new VulnerabilityScript(4, Arrays.asList(1, 2));

            List<VulnerabilityScript> scripts = Arrays.asList(script1, script2, script3, script4);

            List<Integer> expectedPlan = Arrays.asList(3, 2, 1, 4);

            boolean allPassed = true;

            for (int i = 0; i < 5; i++) {
                Collections.shuffle(scripts);
                List<Integer> executionPlan = ScriptExecutionSolver.createExecutionPlan(scripts);

                if (!executionPlan.equals(expectedPlan)) {
                    System.err.println("testShuffledInput failed on iteration " + (i + 1));
                    System.err.println("Expected: " + expectedPlan);
                    System.err.println("Got: " + executionPlan);
                    allPassed = false;
                    break;
                }
            }

            if (allPassed) {
                System.out.println("testShuffledInput passed.");
            }
        } catch (Exception e) {
            System.err.println("testShuffledInput failed with exception: " + e.getMessage());
        }
    }
}
