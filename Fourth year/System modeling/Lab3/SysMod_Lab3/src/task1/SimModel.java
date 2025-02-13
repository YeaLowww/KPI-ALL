package task1;
import java.util.ArrayList;
import java.util.PriorityQueue;

public class SimModel {
    public static void main(String[] args) {
        Double TIME = 100.0;

        Create c = new Create(0.5, 1);
        c.setName("Creator");
        c.setDistribution("exp");
        c.putTnext(0.1);

        Process p1 = new Process(1, 1, 2);
        p1.setName("Line 1");
        p1.setMaxqueue(4);
        p1.setDistribution("norm");
        p1.setDelayDev(0.3);
        p1.setQueue(3);

        Process p2 = new Process(1, 1, 2);
        p2.setName("Line 2");
        p2.setMaxqueue(4);
        p2.setDistribution("norm");
        p2.setDelayDev(0.3);
        p1.setQueue(3);

        Dispose d = new Dispose();
        d.setName("Dispose");

        PriorityQueue<QueueElement> queue = new PriorityQueue<>();
        queue.add(new QueueElement(p1, 0));
        queue.add(new QueueElement(p2, -1));
        c.setNextElementQueue(queue);
        p1.setNextElement(d);
        p2.setNextElement(d);

        ArrayList<Element> list = new ArrayList<>();
        list.add(c);
        list.add(p1);
        list.add(p2);
        list.add(d);

//      populate initial queues
        p1.inAct();
        p1.inAct();
        p1.inAct();
        p1.setDistribution("exp");
        p1.setDelayMean(0.3);
        p2.inAct();
        p2.inAct();
        p2.inAct();
        p2.setDistribution("exp");
        p2.setDelayMean(0.3);

        new Model(list).simulate(TIME);

        for (Element e : list) {
            if (e instanceof Process) {
                Process p = (Process) e;
                System.out.println(String.format(
                        ">>>     %s     <<<\n" +
                        "1. Середнє завантаження: %.4f\n" +
                        "2. Середнє число клієнтів: %.4f\n" +
                        "3. Середній інтервал часу між від'їздами клієнтів: %.4f\n" +
                        "4. Середній час перебування клієнта в банку: %.4f\n" +
                        "5. Середнє число клієнтів: %.4f\n" +
                        "6. Відсоток клієнтів, яким відмовлено в обслуговуванні: %.4f\n" +
                        "7. Число змін смуги: %d\n",
                        p.getName(),
                        p.stateSum / TIME,
                        (p.getMeanQueue() + p.stateSum) / TIME,
                        TIME / p.getQuantity(),
                        (p.getMeanQueue() / TIME + 2) * (TIME - p.waitTime) / p.getQuantity(),
                        p.getMeanQueue() / TIME,
                        p.getFailure() / (double) (p.getFailure() + p.getQuantity()),
                        p.swapCount
                ));
            }
        }
    }
}
