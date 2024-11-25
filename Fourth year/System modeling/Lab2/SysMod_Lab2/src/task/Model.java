package task;
import java.util.ArrayList;

public class Model {
    private final ArrayList<Element> list;
    double tnext, tcurr;

    public Model(ArrayList<Element> elements) {
        list = elements;
        tnext = 0.0;
        tcurr = tnext;
    }

    public void simulate(double time) {
//        starting message
        System.out.println("Current simulation elements are:");
        for (Element element : list) {
            System.out.println(String.format(
                    "- %s (id %s)",
                    element.getName(), element.getId()));
        }

        int i = 0;
        while (tcurr < time) {
            i++;
//            searching for nearest event
            tnext = Double.MAX_VALUE;
            int eventId = 0;
            for (Element e : list) {
                if (e.getTnext() < tnext) {
//                    current time
                    tnext = e.getTnext();
                    eventId = e.getId();
                }
            }

            System.out.println(String.format(
                    "\n"+
                    ">>>     Event in %s     <<<\n"+
                    ">>>     time: %.4f     <<<",
                    list.get(eventId).getName(), tnext));
            for (Element e : list) {
                e.doStatistics(tnext - tcurr);
            }
//            updating the current time
            tcurr = tnext;
            for (Element e : list) {
                e.setTcurr(tcurr);
            }
//            call the outAct()
            list.get(eventId).outAct();
            for (Element e : list) {
                if (e.getTnext() == tcurr) {
                    e.outAct();
                }
            }
            printInfo();
        }
        printResult();
    }

    public void printInfo() {
        for (Element e : list) {
            e.printInfo();
        }
    }

    public void printResult() {
        System.out.println("\n-------------RESULTS-------------");
        for (Element e : list) {
            e.printResult();
            if (e instanceof Process) {
                Process p = (Process) e;
                System.out.println(String.format(
                        "Mean length of queue = %.3f\n" +
                        "Failure probability = %.3f\n",
                        p.getMeanQueue() / tcurr,
                        p.getFailure() / (double) (p.getFailure() + p.getQuantity())));
            } else {
                System.out.println();
            }
        }
    }

}