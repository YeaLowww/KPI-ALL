package hospital;

import java.util.*;

public class Process extends Element {
    protected int workersAmount, busyWorkersAmount;
    protected ArrayList<Patient> tNextList;
    protected LinkedList<Patient> queue;
    protected double meanQueue, meanLoad;

    public Process(String name) {
        super(name);
        workersAmount = Integer.MAX_VALUE;
        busyWorkersAmount = 0;
        queue = new LinkedList<>();
        tNextList = new ArrayList<>();
        meanQueue = 0.0;
        meanLoad = 0.0;
    }

    public Process(String name,  int workersAmount) {
        this(name);
        this.workersAmount = workersAmount;
    }

    @Override
    public void inAct(Patient patient) {
        if (busyWorkersAmount < workersAmount) {
            super.outAct();
            busyWorkersAmount++;

            patient.setTNext(tCurrent + getDelay(patient));
            tNextList.add(patient);
        } else {
            queue.add(patient);
        }
    }

    @Override
    public void outAct() {
        Patient processedPatient = getPatientWithMinTNext();
        tNextList.remove(processedPatient);
        busyWorkersAmount--;

        if (!queue.isEmpty()) {
            super.outAct();
            busyWorkersAmount++;

            Patient patient = getPatientFromQueue();
            patient.setTNext(tCurrent + getDelay(patient));
            tNextList.add(patient);
        }

        goToNextElement(processedPatient);
    }

    @Override
    public double getTNext() {
        Patient patient = getPatientWithMinTNext();
        return patient != null ? patient.getTNext() : Integer.MAX_VALUE;
    }
    @Override
    public void doStatistics(double delta) {
        this.meanQueue = meanQueue + queue.size() * delta;
        this.meanLoad = meanLoad + busyWorkersAmount * delta;
    }

    protected Patient getPatientFromQueue() {
        return queue.poll();
    }
    protected void goToNextElement(Patient patient) {
        if(nextElement != null) {
            nextElement.inAct(patient);
        }
    }

    public double getMeanLoad() {
        return meanLoad;
    }
    public double getMeanQueue() {
        return  meanQueue;
    }

    private Patient getPatientWithMinTNext() {
        if (tNextList.isEmpty()) {
            return null;
        }

        Patient minTNextPatient = tNextList.get(0);

        for (Patient patient : tNextList) {
            if (patient.getTNext() < minTNextPatient.getTNext()) {
                minTNextPatient = patient;
            }
        }

        return minTNextPatient;
    }
}