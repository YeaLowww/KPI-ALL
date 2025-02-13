package hospital;

import java.util.Random;

public class Create extends Element {
    private double[] probabilities;

    public Create(String name) {
        super(name);
        probabilities = new double[]{ 0.5, 0.1, 0.4 };
    }

    @Override
    public void outAct() {
        super.outAct();
        tNext = tCurrent + getDelay(null);

        Patient nextPatient = getNextPatient();
        if(nextElement != null) {
            nextElement.inAct(nextPatient);
        }
    }

    private Patient getNextPatient() {
        Random random = new Random();
        double randomValue = random.nextDouble();
        double sumProbability = 0.0;

        for (int i = 0; i < probabilities.length; i++) {
            sumProbability += probabilities[i];
            if (randomValue < sumProbability) {
                return new Patient(tCurrent, i + 1);
            }
        }

        throw new RuntimeException("Invalid!");
    }
}