package hospital;

public class Element {
    protected String name;
    protected double tNext, tCurrent;
    protected int quantity;
    protected Element nextElement;
    protected double delayMean, delayDeviation;
    protected int k;

    private String distribution;
    private static int nextId = 0;
    private int id;

    public Element(String name) {
        this.name = name;
        tNext = 0.0;
        tCurrent = tNext;
        nextElement = null;
        id = nextId;
        nextId++;
    }

    protected double getDelay(Patient patient) {
        switch (distribution) {
            case "exp":
                return FunRand.Exp(delayMean);
            case "norm":
                return FunRand.Norm(delayMean, delayDeviation);
            case "unif":
                return FunRand.Unif(delayMean, delayDeviation);
            case "erlang":
                return FunRand.Erlang(delayMean, k);
        }

        return delayMean;
    }

    public void Exp_Distribution(double delayMean) {
        distribution = "exp";
        this.delayMean = delayMean;
    }
    public void Norm_Distribution(double delayMean, double delayDeviation) {
        distribution = "norm";
        this.delayMean = delayMean;
        this.delayDeviation = delayDeviation;
    }
    public void Unif_Distribution(double delayMean, double delayDeviation) {
        distribution = "unif";
        this.delayMean = delayMean;
        this.delayDeviation = delayDeviation;
    }
    public void Erlang_Distribution(double delayMean, int k) {
        distribution = "erlang";
        this.delayMean = delayMean;
        this.k = k;
    }

    public int getQuantity() {
        return quantity;
    }
    public void setTCurrent(double tCurrent) {
        this.tCurrent = tCurrent;
    }
    public void setNextElement(Element nextElement) {
        this.nextElement = nextElement;
    }
    public void inAct(Patient patient) {
    }
    public void outAct(){
        quantity++;
    }
    public double getTNext() {
        return tNext;
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
