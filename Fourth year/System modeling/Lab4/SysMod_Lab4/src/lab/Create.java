package lab;
public class Create extends Element {
    protected Element nextElement;

    public Create(String name, double delay) {
        super(name, delay);
    }

    @Override
    public void outAct() {
        super.outAct();
        tNext = tCurrent + getDelay();

        if(nextElement != null) {
            nextElement.inAct();
        }
    }

    public void setNextElement(Element nextElement) {
        this.nextElement = nextElement;
    }
}