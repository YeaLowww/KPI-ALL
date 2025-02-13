package task2;

import  task2.models.Patient;
import  task2.utils.Consts;
import task2.models.QueueElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Random;

public class Create extends Element {
    private double lastPatientArrivalTime = 0;

    public Create(double delay, int workerCount) {
        super(delay, workerCount, Integer.MAX_VALUE);
        super.putTnext(0.0);
    }

    @Override
    public void outAct() {
        super.outAct();
        super.putTnext(super.getTcurr() + super.getDelay());

        Patient patient = new Patient(
                super.getQuantity(), new Random().nextDouble(), super.getTcurr());
        System.out.println(String.format(
                ">>>     Patient #%d arrived after %.4f time since the last one     <<<",
                patient.id, super.getTcurr() - lastPatientArrivalTime));
        lastPatientArrivalTime = super.getTcurr();

        switch (super.nextElementType()) {
            case Consts.NextElementType.single -> {
                Process nextElement = (Process) super.getNextElement();
                nextElement.nextPatients.add(patient);
                nextElement.inAct();
            }
//            case Consts.NextElementType.queue -> {
//                final PriorityQueue<QueueElement> queue = super.getNextElementQueue();
//                QueueElement nextQueueElement = null;
//                int smallest_size = Integer.MAX_VALUE;
//                for (QueueElement queueElement : queue) {
//                    if (queueElement.element.getQueue() < smallest_size) {
//                        nextQueueElement = queueElement;
//                        smallest_size = queueElement.element.getQueue();
//                    }
//                }
//                assert nextQueueElement != null;
//                nextQueueElement.element.inAct();
//                nextQueueElement.priority = nextQueueElement.element.getTnext();
//            }
//            case Consts.NextElementType.random -> {
//                final HashMap<Double, Element> list = super.getNextRandomElementArray();
//                final double chance = new Random().nextDouble();
//                double sum = 0;
//                for (Double key : list.keySet()) {
//                    sum += key;
//                    if (chance < sum) {
//                        list.get(key).inAct();
//                        break;
//                    }
//                }
//            }
//            case Consts.NextElementType.typed -> {
//                Element nextElement = super.getNextTypedElementArray().get(patientType);
//                if (nextElement instanceof Process) {
//                    Process nextProcess = (Process) nextElement;
//                    nextProcess.nextPatientType.add(patientType);
//                    nextProcess.inAct();
//                } else if (nextElement instanceof Dispose) {
//                    nextElement.inAct();
//                }
//            }
        }
        super.popTnextQueue();
    }
}
