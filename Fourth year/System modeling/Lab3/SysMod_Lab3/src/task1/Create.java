package task1;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Random;

public class Create extends Element {
    public Create(double delay, int workerCount) {
        super(delay, workerCount, Integer.MAX_VALUE);
        super.putTnext(0.0);
    }

    @Override
    public void outAct() {
        super.outAct();
        super.putTnext(super.getTcurr() + super.getDelay());
        switch (super.nextElementType()) {
            case NextElementType.single -> {
                super.getNextElement().inAct();
            }
            case NextElementType.queue -> {
                final PriorityQueue<QueueElement> queue = super.getNextElementQueue();
                QueueElement nextQueueElement = null;
                int smallest_size = Integer.MAX_VALUE;
                for (QueueElement queueElement : queue) { //пошук черги з мін к к
                    if (queueElement.element.getQueue() < smallest_size) {
                        nextQueueElement = queueElement;
                        smallest_size = queueElement.element.getQueue();
                    }
                }
                assert nextQueueElement != null;
                nextQueueElement.element.inAct();
                nextQueueElement.priority = nextQueueElement.element.getTnext();
            }
            case NextElementType.random -> {
                final HashMap<Double, Element> list = super.getNextRandomElementArray();
                final double chance = new Random().nextDouble();
                double sum = 0;
                for (Double key : list.keySet()) {
                    sum += key;
                    if (chance < sum) {
                        list.get(key).inAct();
                        break;
                    }
                }
            }
        }
        super.popTnextQueue();
    }
}
