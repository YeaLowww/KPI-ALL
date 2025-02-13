
package lab;
import java.util.ArrayList;

public class Model {

    private ArrayList<Element> elements;
    private double tNext, tCurrent;
    private int event;

    public Model(ArrayList<Element> elements) {
        this.elements = elements;
        tNext = 0.0;
        tCurrent = tNext;
        event = 0;
    }

    public void simulate(double time) {
        while (tCurrent < time) {
            tNext = Double.MAX_VALUE;
            for (Element e : elements) {
                if (e.getTnext() < tNext) {
                    tNext = e.getTnext();
                    event = e.getId();
                }
            }
            for (Element e : elements) {
                e.doStatistics(tNext - tCurrent);
            }
            tCurrent = tNext;
            for (Element e : elements) {
                e.setTcurr(tCurrent);
            }
            elements.get(event).outAct();
            for (Element e : elements) {
                if (e.getTnext() == tCurrent) {
                    e.outAct();
                }
            }
        }
        //printResult();
    }
    public void printResult() {
        System.out.println("\n-----------------------RESULTS-----------------------");

        for (Element e : elements) {
            System.out.println("-----------------------");
            System.out.println(e.getName());
            if (e instanceof Process) {
                Process p = (Process) e;
                System.out.println("Quantity: " + e.getQuantity() + ";");
                System.out.println("Average queue: " + p.getMeanQueue() / tCurrent + ";");
                System.out.println("Average proc load: " + p.getMeanLoad() / tCurrent + ";");
            }
        }
    }
}
