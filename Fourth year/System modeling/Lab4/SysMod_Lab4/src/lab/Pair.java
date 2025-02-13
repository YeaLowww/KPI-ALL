package lab;
public class Pair {
    private Element element;
    private double probabilityOrPriority;

    public Pair(Element element, double probabilityOrPriority) {
        this.element = element;
        this.probabilityOrPriority = probabilityOrPriority;
    }

    public Element getElement() {
        return element;
    }
    public double getProbabilityOrPriority() {
        return probabilityOrPriority;
    }
}
