package hospital;

import java.util.Random;

public class FunRand {
    public static double Exp(double timeMean) {
        double a = 0;
        while (a == 0) a = Math.random();
        return -timeMean * Math.log(a);
    }

    public static double Unif(double timeMin, double timeMax) {
        double a = 0;
        while (a == 0) a = Math.random();
        return timeMin + a * (timeMax - timeMin);
    }

    public static double Norm(double timeMean, double timeDeviation) {
        double a = 0;
        Random r = new Random();
        while (a <= 0) a = timeMean + timeDeviation * r.nextGaussian();
        return a;
    }

    public static double Erlang(double timeMean, double k) {
        double sum = 0;
        for (int i = 0; i < k; i++) {
            double a = 0;
            while (a == 0) {
                a = Math.random();
            }
            a = -timeMean * Math.log(a);
            sum += a;
        }

        return sum;
    }
}