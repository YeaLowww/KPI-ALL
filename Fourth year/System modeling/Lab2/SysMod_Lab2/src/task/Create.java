package task;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Random;

public class Create extends Element {
    public Create(double delay, int workerCount) {
        super(delay, workerCount);
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
                QueueElement nextQueueElement = queue.poll();
                assert nextQueueElement != null;
                nextQueueElement.element.inAct();
                nextQueueElement.priority = nextQueueElement.element.getTnext();
                queue.add(nextQueueElement);
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
