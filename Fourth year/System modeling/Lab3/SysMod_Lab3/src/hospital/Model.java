package hospital;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Model {
    private final DecimalFormat df = new DecimalFormat("0.00");
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
                if (e.getTNext() < tNext) {
                    tNext = e.getTNext();
                    event = e.getId();
                }
            }

            System.out.println("It's time for event in " + elements.get(event).getName() + ", time = " + tNext);
            for (Element e : elements) {
                e.doStatistics(tNext - tCurrent);
            }

            tCurrent = tNext;
            for (Element e : elements) {
                e.setTCurrent(tCurrent);
            }

            elements.get(event).outAct();
            for (Element e : elements) {
                if (e.getTNext() == tCurrent) {
                    e.outAct();
                }
            }
        }

        printResult();
    }

    public void printResult() {
        System.out.println("\n-----------------------Results-----------------------");

        for (Element e : elements) {
            System.out.println("-------------------------");
            System.out.println(e.getName());

            System.out.println("Quantity: " + e.getQuantity() + ";");
            if (e instanceof Process) {
                Process p = (Process) e;
                System.out.println("Average queue: " + df.format(p.getMeanQueue() / tCurrent) + ";");
                System.out.println("Avevarage load: " + df.format(p.getMeanLoad() / tCurrent) + ";");
            }
        }
    }
}
