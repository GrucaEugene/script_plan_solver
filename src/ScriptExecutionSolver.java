import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ScriptExecutionSolver {

    public static List<Integer> createExecutionPlan(List<VulnerabilityScript> scripts) {
        var scriptMap = scripts.stream()
                .collect(Collectors.toMap(VulnerabilityScript::scriptId, Function.identity()));

        var resultList = new ArrayList<Integer>();
        var processedSet = new HashSet<Integer>();
        var pendingSet = new HashSet<Integer>();

        scriptMap.forEach((key, _) -> {
            if (!processedSet.contains(key)) {
                processScript(key, scriptMap, resultList, processedSet, pendingSet);
            }
        });

        return resultList;
    }

    private static void processScript(
            Integer scriptId,
            Map<Integer, VulnerabilityScript> scriptMap,
            ArrayList<Integer> resultList,
            HashSet<Integer> processedSet,
            HashSet<Integer> pendingSet) {

        if (processedSet.contains(scriptId)) {
            return;
        }

        if (pendingSet.contains(scriptId)) {
            throw new RuntimeException("Circular dependency detected involving script with id: " + scriptId);
        }

        pendingSet.add(scriptId);

        var script = Optional.ofNullable(scriptMap.get(scriptId))
                .orElseThrow(() -> new RuntimeException("Script " + scriptId + " not found"));

        script.dependencies()
                .forEach(dependency -> processScript(dependency, scriptMap, resultList, processedSet, pendingSet));

        resultList.add(scriptId);
        processedSet.add(scriptId);
        pendingSet.remove(scriptId);
    }


    public static void main(String[] args) {
        VulnerabilityScript script1 = new VulnerabilityScript(1, Arrays.asList(2, 3));
        VulnerabilityScript script2 = new VulnerabilityScript(2, Arrays.asList(3));
        VulnerabilityScript script3 = new VulnerabilityScript(3, Collections.emptyList());
        VulnerabilityScript script4 = new VulnerabilityScript(4, Arrays.asList(1, 2));
//        VulnerabilityScript script5 = new VulnerabilityScript(5, Arrays.asList(6));
//        VulnerabilityScript script6 = new VulnerabilityScript(6, Arrays.asList(5));

        List<VulnerabilityScript> scripts = Arrays.asList(script1, script2, script3, script4);
        Collections.shuffle(scripts);

        List<Integer> executionPlan = createExecutionPlan(scripts);

        System.out.println("Execution Plan: " + executionPlan);
    }
}

record VulnerabilityScript(int scriptId, List<Integer> dependencies) {
}



