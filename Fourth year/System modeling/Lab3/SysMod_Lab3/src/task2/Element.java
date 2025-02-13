package task2;

import  task2.utils.Consts;
import  task2.utils.Consts.DistributionType;
import  task2.models.QueueElement;
import  task2.utils.FunRand;

import java.util.HashMap;
import java.util.PriorityQueue;

public class Element {
    private String name;
    private final PriorityQueue<Double> tnext;
    private double delayMean, delayDev;
    private int distribution;
    private int quantity;
    private double tcurr;
    private int state;
    private Element nextElement;
    private PriorityQueue<QueueElement> nextElementQueue;
    private HashMap<Double, Element> nextRandomElement;
    private HashMap<Integer, Element> nextTypedElement;
    private int nextElementType;
    private static int nextId = 0;
    private int id;
    private int queue;
    private double avgLoad;

    public final int workerCount;
    public final int swapThreshold;
    public int k;


//    public lab.Element() {
//
//        tnext = Double.MAX_VALUE;
//        delayMean = 1.0;
//        distribution = "exp";
//        tcurr = tnext;
//        state = 0;
//        nextElement = null;
//        id = nextId;
//        nextId++;
//        name = "element" + id;
//    }

    public Element(double delay, int workerCount, int swapThreshold) {
        name = "Anonymous";
        tnext = new PriorityQueue<>();
        this.workerCount = workerCount;
        this.swapThreshold = swapThreshold;
        delayMean = delay;
        tcurr = 0.0;
        state = 0;
        queue = 0;
        avgLoad = 0;
        k = 0;
        nextElement = null;
        id = nextId;
        nextId++;
        name = "element" + id;
    }

//    public lab.Element(String nameOfElement, double delay) {
//        name = nameOfElement;
//        tnext = 0.0;
//        delayMean = delay;
//        distribution = "exp";
//        tcurr = tnext;
//        state = 0;
//        nextElement = null;
//        id = nextId;
//        nextId++;
//        name = "element" + id;
//    }

    public double getDelay() {
        switch (getDistribution()) {
            case DistributionType.exponential -> {
                return FunRand.Exp(getDelayMean());
            }
            case DistributionType.normal -> {
                return FunRand.Norm(getDelayMean(), getDelayDev());
            }
            case DistributionType.uniform -> {
                return FunRand.Unif(getDelayMean(), getDelayDev());
            }
            case DistributionType.erlang -> {
                return FunRand.Erlang(getDelayMean(), k);
            }
            default -> {
                return getDelayMean();
            }
        }
    }

    public double getDelayDev() {
        return delayDev;
    }

    public void setDelayDev(double delayDev) {
        this.delayDev = delayDev;
    }

    public int getDistribution() {
        return distribution;
    }

    public void setDistribution(int distribution) {
        this.distribution = distribution;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getTcurr() {
        return tcurr;
    }

    public void setTcurr(double tcurr) {
        this.tcurr = tcurr;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Element getNextElement() {
        return nextElement;
    }

    public PriorityQueue<QueueElement> getNextElementQueue() {
        return nextElementQueue;
    }

    public HashMap<Double, Element> getNextRandomElementArray() {
        return nextRandomElement;
    }

    public HashMap<Integer, Element> getNextTypedElementArray() {
        return nextTypedElement;
    }

    public void setNextElement(Element nextElement) {
        this.nextElementType = Consts.NextElementType.single;
        this.nextElement = nextElement;
    }

    public void setNextElementQueue(PriorityQueue<QueueElement> nextElementQueue) {
        this.nextElementType = Consts.NextElementType.queue;
        this.nextElementQueue = nextElementQueue;
    }

    public void setNextRandomElementArray(HashMap<Double, Element> nextRandomElement) {
//        check if sum of keys is equal to 1
        double sum = 0;
        for (Double key : nextRandomElement.keySet()) {
            sum += key;
        }
        assert sum == 1;

        this.nextElementType = Consts.NextElementType.random;
        this.nextRandomElement = nextRandomElement;
    }

    public void setNextTypedElementArray(HashMap<Integer, Element> nextTypedElement) {
        this.nextElementType = Consts.NextElementType.typed;
        this.nextTypedElement = nextTypedElement;
    }

    public int nextElementType(){
        return this.nextElementType;
    }

    public void inAct() {

    }

    public void outAct() {
        quantity++;
    }

    public double getTnext() {
        return this.tnext.peek();
    }

    public void putTnext(double tnext) {
        this.tnext.add(tnext);
    }

    public Double popTnextQueue() {
        return this.tnext.poll();
    }

    public double getDelayMean() {
        return delayMean;
    }

    public void setDelayMean(double delayMean) {
        this.delayMean = delayMean;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void printResult() {
        System.out.println(getName() + " quantity = " + quantity);
    }

    private void updateAvgLoad() {
        avgLoad = quantity / tnext.peek();
    }

    public void printInfo() {
        if (tnext.peek() != Double.MAX_VALUE) {
            updateAvgLoad();
        }

        System.out.println(String.format(
                "##### %s #####",
                getName()));

        if (this instanceof Dispose) {
            System.out.println(String.format(
                    "quantity: %d",
                    quantity));
        } else {
            System.out.println(String.format(
                    "state: %d | quantity: %d | queue: %d | tnext: %5.4f | avgLoad: %.4f",
                    state, quantity, queue, tnext.peek(), avgLoad));
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void doStatistics(double delta) {}

    public int getQueue() {
        return queue;
    }

    public void setQueue(int queue) {
        this.queue = queue;
    }
}