package task;

import java.util.Arrays;
import java.util.HashMap;
import java.util.PriorityQueue;

public class Element {
    private String name;
    private final PriorityQueue<Double> tnext;
    private double delayMean, delayDev;
    private String distribution;
    private int quantity;
    private double tcurr;
    private int state;
    private Element nextElement;
    private PriorityQueue<QueueElement> nextElementQueue;
    private HashMap<Double, Element> nextRandomElement;
    private int nextElementType;
    private static int nextId = 0;
    private int id;

    public int workerCount;
// workerCount - максимальна кількість одночасних процесів
// які можуть бути активними в даний момент
// якщо поточна кількість активних завдань
// що зберігається в super.getState()) досягає ліміту
// нові запити ставляться в чергу

//    public Element() {
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

    public Element(double delay, int workerCount) {
        name = "Anonymous";
        tnext = new PriorityQueue<>();
        this.workerCount = workerCount;
        delayMean = delay;
        distribution = "";
        tcurr = 0.0;
        state = 0;
        nextElement = null;
        id = nextId;
        nextId++;
        name = "element" + id;
    }

//    public Element(String nameOfElement, double delay) {
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
        double delay = getDelayMean();
        if ("exp".equalsIgnoreCase(getDistribution())) {
            delay = FunRand.Exp(getDelayMean());
        } else {
            if ("norm".equalsIgnoreCase(getDistribution())) {
                delay = FunRand.Norm(getDelayMean(),
                        getDelayDev());
            } else {
                if ("unif".equalsIgnoreCase(getDistribution())) {
                    delay = FunRand.Unif(getDelayMean(),
                            getDelayDev());
                } else {
                    if ("".equalsIgnoreCase(getDistribution()))
                        delay = getDelayMean();
                }
            }
        }
        return delay;
    }


    public double getDelayDev() {
        return delayDev;
    }

    public void setDelayDev(double delayDev) {
        this.delayDev = delayDev;
    }

    public String getDistribution() {
        return distribution;
    }

    public void setDistribution(String distribution) {
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

    public void setNextElement(Element nextElement) {
        this.nextElementType = NextElementType.single;
        this.nextElement = nextElement;
    }

    public void setNextElementQueue(PriorityQueue<QueueElement> nextElementQueue) {
        this.nextElementType = NextElementType.queue;
        this.nextElementQueue = nextElementQueue;
    }

    public void setNextRandomElementArray(HashMap<Double, Element> nextRandomElement) {
//        check if sum of keys is equal to 1
        double sum = 0;
        for (Double key : nextRandomElement.keySet()) {
            sum += key;
        }
        assert sum == 1;

        this.nextElementType = NextElementType.random;
        this.nextRandomElement = nextRandomElement;
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

    public void printInfo() {

        System.out.println(String.format(
                "##### %s #####",
                getName()));

        if (this instanceof Dispose) {
            System.out.println(String.format(
                    "quantity: %d",
                    quantity));
        } else {
            System.out.println(String.format(
                    "state: %d | quantity: %d | tnext: %5.4f | avgLoad: %.4f",
                    state, quantity, tnext.peek(), quantity / tnext.peek()));
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void doStatistics(double delta) {

    }
}