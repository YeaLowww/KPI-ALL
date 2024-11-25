package task_kr;

import java.util.Random;

public class Main {
    public static void main(String[] args) {
        // Пари значень (x, P(x))
        double[][] pairs = {
                {0, 0},
                {2, 0.3},
                {4, 0.7},
                {6, 0.9},
                {10, 1.0}
        };

        int numSamples = 10; // Кількість зразків для генерації
        double[] results = new double[numSamples];

        Random random = new Random();

        // Генерація
        for (int i = 0; i < numSamples; i++) {
            double randNum = random.nextDouble();
            results[i] = getValueFromCDF(pairs, randNum);
        }
        for (double value : results) {
            System.out.println(value);
        }
    }

    // Метод CDF
    private static double getValueFromCDF(double[][] pairs, double randNum) {
        for (int i = 0; i < pairs.length; i++) {
            if (randNum <= pairs[i][1]) {
                return pairs[i][0]; // значення x
            }
        }
        return pairs[pairs.length - 1][0]; //  останнє значення x, якщо randNum = 1
    }
}
