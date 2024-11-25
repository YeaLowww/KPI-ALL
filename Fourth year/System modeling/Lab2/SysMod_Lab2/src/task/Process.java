package task;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Random;

public class Process extends Element {
    private int queue, maxqueue, failure;
    private double meanQueue;

    public Process(double delay, int workerCount) {
        super(delay, workerCount);
        queue = 0;
        maxqueue = Integer.MAX_VALUE;
        meanQueue = 0.0;
        super.putTnext(Double.MAX_VALUE);
    }

    @Override
    public void inAct() {
        if (super.getState() < super.workerCount) {
            super.setState(super.getState() + 1);
            super.putTnext(super.getTcurr() + super.getDelay());
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
        }
//        practically the same as in Create
        switch (super.nextElementType()) {
            case NextElementType.single -> {
                super.getNextElement().inAct();
            }
            case NextElementType.queue -> {
                final PriorityQueue<QueueElement> queue = super.getNextElementQueue();
                QueueElement nextQueueElement = queue.poll();
                assert nextQueueElement != null;
                nextQueueElement.element.inAct();
                nextQueueElement.priority = nextQueueElement.element.getTnext();
                queue.add(nextQueueElement);
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

    public int getQueue() {
        return queue;
    }

    public void setQueue(int queue) {
        this.queue = queue;
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
        if (!super.getName().equalsIgnoreCase("dispose")) {
            System.out.println("failure: " + this.getFailure());
        }
    }

    @Override
    public void doStatistics(double delta) {
        meanQueue = getMeanQueue() + queue * delta;
    }

    public double getMeanQueue() {
        return meanQueue;
    }
}