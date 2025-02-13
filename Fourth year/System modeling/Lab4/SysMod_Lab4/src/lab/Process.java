package lab;
import java.util.*;

public class Process extends Element {
    private int queue;
    private double meanQueue;
    private double meanLoad;
    private ArrayList<Pair> nextElements;
    private boolean isAvailable;
    private TransitionType type;

    public Process(String name, double delay) {
        super(name, delay);
        queue = 0;
        meanQueue = 0.0;
        meanLoad = 0.0;
        nextElements = new ArrayList<>();
        isAvailable = true;

        type = TransitionType.Probability;
    }
    public Process(String name, double delay, TransitionType type) {
        this(name, delay);
        this.type = type;
    }

    @Override
    public void inAct() {
        if (isAvailable) {
            super.outAct();
            isAvailable = false;

            tNext = tCurrent + getDelay();
        } else {
            queue++;
        }
    }
    @Override
    public void outAct() {
        isAvailable = true;

        if (queue > 0) {
            super.outAct();
            isAvailable = false;
            queue--;

            tNext = tCurrent + getDelay();
        } else {
            tNext = Integer.MAX_VALUE;
        }

        Element nextElement = getNextElement();
        if(nextElement != null) {
            nextElement.inAct();
        }
    }

    public void addElement(Pair pair) {
        nextElements.add(pair);
    }
    public void addElements(List<Pair> pairs) {
        nextElements.addAll(pairs);
    }
    @Override
    public void doStatistics(double delta) {
        this.meanQueue = getMeanQueue() + queue * delta;
        this.meanLoad = this.getMeanLoad() + delta;
    }
    public double getMeanQueue() {
        return this.meanQueue;
    }
    public double getMeanLoad() {
        return this.meanLoad;
    }

    private Element getNextElement() {
        if (nextElements.isEmpty()) {
            return null;
        }

        switch (type){
            case Probability:
                return getNextElementByProbability();
            case Priority:
                return getNextElementByPriority();
        }

        throw new RuntimeException();
    }

    private Element getNextElementByProbability() {
        double totalProbability = 0;

        for (Pair pair : nextElements) {
            totalProbability += pair.getProbabilityOrPriority();
        }

        double randomValue = new Random().nextDouble() * totalProbability;

        double cumulativeProbability = 0;

        for (Pair pair : nextElements) {
            cumulativeProbability += pair.getProbabilityOrPriority();
            if (randomValue <= cumulativeProbability) {
                return pair.getElement();
            }
        }

        throw new RuntimeException();
    }



    private Element getNextElementByPriority() {
        Collections.sort(nextElements, Comparator.comparingDouble(Pair::getProbabilityOrPriority));

        int indexOfLastElement = nextElements.size() - 1;
        return nextElements.remove(indexOfLastElement).getElement();
    }
}