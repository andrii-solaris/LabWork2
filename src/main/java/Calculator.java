import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class Calculator {

    public static void main(String[] args) {
        var equations = new EquationHelper();

        System.out.println(
                "Average operating time is: " +
                equations.doubleToPrecision(equations.calculateAverageOperatingTime(10, -4), 3)
        );

        System.out.println(
                "System readiness ratio is: " +
                        equations.doubleToPrecision(equations.calculateSystemReadinessRatio(), 2)
        );

        System.out.println("System readiness function is: ");

        var treeMap = new TreeMap(equations.calculateSystemReadinessFunction(10, -4, 100));

        Set set = treeMap.entrySet();
        for (Object o : set) {
            var entry = (Map.Entry<Integer, Double>) o;
            var key = (Integer) entry.getKey();
            var value = (Double) entry.getValue();
            System.out.println("|" + key + " || " + value + "|");
        }

        System.out.println(
                "System readiness ratio is: " +
                        equations.calculateRiskRatio(10, -5)
        );
    }
}
