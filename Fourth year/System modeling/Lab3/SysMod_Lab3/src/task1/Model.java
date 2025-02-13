package task1;
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

        while (tcurr < time) {
//            balance the process queues
//            hardcodе
            Process line1 = (Process) list.get(1);
            Process line2 = (Process) list.get(2);
            if (line1.getQueue() - line2.getQueue() > line1.swapThreshold) {
                line1.setQueue(line1.getQueue()-1);
                line2.setQueue(line2.getQueue()+1);
                line1.swapCount++;
                System.out.println("\n>>>     Moved a car from line 1 to line 2!     <<<");
            } else if (line2.getQueue() - line1.getQueue() > line2.swapThreshold) {
                line1.setQueue(line1.getQueue()+1);
                line2.setQueue(line2.getQueue()-1);
                line2.swapCount++;
                System.out.println("\n>>>     Moved a car from line 2 to line 1!     <<<");
            }

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
        System.out.println("The simulation has ended!\n");
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