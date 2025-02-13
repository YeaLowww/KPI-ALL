package task1;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Random;

public class Process extends Element {
    private int maxqueue, failure;
    private double meanQueue;
    private double waitStart;

    public double stateSum;
    public double waitTime;
    public int swapCount;

    public Process(double delay, int workerCount, int swapThreshold) {
        super(delay, workerCount, swapThreshold);
        maxqueue = Integer.MAX_VALUE;
        meanQueue = 0.0;
        super.putTnext(Double.MAX_VALUE);
    }

    @Override
    public void inAct() {
        if (super.getState() < super.workerCount) {
            super.setState(super.getState() + 1);
            super.putTnext(super.getTcurr() + super.getDelay());
            if (super.getState() == 0) {
                waitTime += getTcurr() - waitStart;
            }
        } else {
            if (getQueue() < getMaxqueue()) {
                setQueue(getQueue() + 1);
            } else {
                failure++;
            }
        }
    }

    @Override
    public void outAct() {
        super.outAct();
        super.setState(super.getState() - 1);
        if (getQueue() > 0) {
            setQueue(getQueue() - 1);
            super.setState(super.getState() + 1);
            super.putTnext(super.getTcurr() + super.getDelay());
        } else {
            waitStart = getTcurr();
        }
//        practically the same as in Create
        switch (super.nextElementType()) {
            case NextElementType.single -> {
                super.getNextElement().inAct();
            }
            case NextElementType.queue -> {
                final PriorityQueue<QueueElement> queue = super.getNextElementQueue();
                QueueElement nextQueueElement = null;
                int smallest_size = Integer.MAX_VALUE;
                for (QueueElement queueElement : queue) {
                    if (queueElement.element.getQueue() < smallest_size) {
                        nextQueueElement = queueElement;
                        smallest_size = queueElement.element.getQueue();
                    }
                }
                assert nextQueueElement != null;
                nextQueueElement.element.inAct();
                nextQueueElement.priority = nextQueueElement.element.getTnext();
            }
            case NextElementType.random -> {
                final HashMap<Double, Element> list = super.getNextRandomElementArray();
                final double chance = new Random().nextDouble();
                double sum = 0;
                for (Double key : list.keySet()) {
                    sum += key;
                    if (chance < sum) {
                        list.get(key).inAct();
                        break;
                    }
                }
            }
        }
        super.popTnextQueue();
    }

    public int getFailure() {
        return failure;
    }

    public int getMaxqueue() {
        return maxqueue;
    }

    public void setMaxqueue(int maxqueue) {
        this.maxqueue = maxqueue;
    }

    @Override
    public void printInfo() {
        super.printInfo();
        System.out.println("failure: " + this.getFailure());
    }

    @Override
    public void doStatistics(double delta) {
        stateSum += delta * getState();
        meanQueue = getMeanQueue() + super.getQueue() * delta;
    }

    public double getMeanQueue() {
        return meanQueue;
    }
}