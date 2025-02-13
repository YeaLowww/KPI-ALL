package task1;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Random;

public class Dispose extends Element {

    public Dispose() {
        super(0, 0, Integer.MAX_VALUE);
        super.putTnext(Double.MAX_VALUE);
    }

    @Override
    public void inAct() {
        this.outAct();
    }

    @Override
    public void outAct() {
        super.outAct();
//        practically the same as in Create
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
    }

    @Override
    public void printInfo() {
        super.printInfo();
    }
}