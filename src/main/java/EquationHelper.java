import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EquationHelper {
    private final Map<String, List<Double>> testData;

    public EquationHelper() {
        this.testData = new ExcelReader().readExcel(0);
    }

    public Double calculateAverageOperatingTime(int number, int power) {
        var lambdas =  testData.get("Failure intensity");

        return Math.pow(number, Math.abs(power)) / this.countRowSum(lambdas);
    }

    public Double calculateSystemReadinessRatio() {
        var lambdas =  testData.get("Failure intensity");
        var mus = testData.get("Restoration intensity");

        var sum = this.countRowsFractionSum(lambdas, mus);

        return 1 / (1 + sum);
    }

    public Map<Integer, Double> calculateSystemReadinessFunction(int number, int power, int timeFractions) {
        var lambdas =  testData.get("Failure intensity");
        var mus = testData.get("Restoration intensity");
        var workingTime = testData.get("Working time");

        var map = new HashMap<Integer, Double>();

        for (var i = 1; i < (int) Math.round(workingTime.get(0)) / timeFractions; i++) {
            var lambdasSum = this.countRowSum(lambdas);
            var muSum = this.countRowsFractionSum(lambdas, mus);

            var mu = lambdasSum / muSum;
            var eulerPower = -(lambdasSum + mu) * Math.pow(number, power) * (i * 100);

            var first = (mu * Math.pow(number, power)) / ((lambdasSum + mu) * Math.pow(number, power));
            var second = (lambdasSum * Math.pow(number, power)) / ((lambdasSum + mu) * Math.pow(number, power));

            map.put(i * 100, first + second * Math.pow(2.718281828459045, eulerPower));
        }

        return map;
    }

    public Double calculateRiskRatio(int number, int power) {
        var lambdas =  testData.get("Failure intensity");
        var mus = testData.get("Restoration intensity");
        var risk = testData.get("Failure risk");

        var muSum = this.countRowsFractionSum(lambdas, mus);
        var lambdasSum = this.countRowSum(lambdas) * Math.pow(number, power);
        var riskSum = this.countRowsMultiplicationSum(lambdas, risk);

        var eulerPower = Math.pow(2.718281828459045, -(lambdasSum) * Math.pow(number, 3));

        return (1 - eulerPower) / lambdasSum * Math.pow(10, -5) * riskSum;
    }

    public String doubleToPrecision(Double value, int precision) {
        DecimalFormat df = new DecimalFormat("#0.00");
        df.setMaximumFractionDigits(precision);

        return df.format(value);
    }

    private Double countRowSum(@NotNull List<Double> list) {
        var sum = 0.00;

        for (Double single : list) {
            sum += single;
        }

        return sum;
    }

    private Double countRowsFractionSum(@NotNull List<Double> numerator, List<Double> denominator) {
        var sum = 0.00;
        var index = 0;

        for (Double single : numerator) {
            sum += single / denominator.get(index++);
        }

        return sum;
    }

    private Double countRowsMultiplicationSum(@NotNull List<Double> multiplicatorOne, List<Double> multiplicatorTwo) {
        var sum = 0.00;
        var index = 0;

        for (Double single : multiplicatorOne) {
            var test = single * multiplicatorTwo.get(index++);

            sum += test;
        }

        return sum;
    }
}
