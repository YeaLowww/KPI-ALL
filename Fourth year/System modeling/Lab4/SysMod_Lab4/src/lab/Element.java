package lab;
public class Element {
    protected String name;
    protected double tNext, tCurrent;
    protected int quantity;

    private double delay;
    private static int nextId = 0;
    private int id;

    public Element(String name, double delay) {
        this.name = name;
        this.delay = delay;
        tNext = 0.0;
        tCurrent = tNext;
        id = nextId;
        nextId++;
    }

    public static void resetNextId() {
        nextId = 0;
    }
    public double getDelay() {
        double a = 0;
        while (a == 0) a = Math.random();
        return -delay * Math.log(a);
    }

    public int getQuantity() {
        return quantity;
    }
    public void setTcurr(double tcurr) {
        this.tCurrent = tcurr;
    }
    public void inAct() {
    }
    public void outAct(){
        quantity++;
    }
    public double getTnext() {
        return tNext;
    }
    public void setTnext(double tnext) {
        this.tNext = tnext;
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }

    public void doStatistics(double delta){
    }
}
