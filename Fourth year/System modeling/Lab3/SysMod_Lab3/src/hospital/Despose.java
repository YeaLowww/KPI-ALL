package hospital;

import java.util.ArrayList;

public class Despose extends Element {
    private final ArrayList<Patient> patients;

    public Despose(String name) {
        super(name);
        patients = new ArrayList<>();
        tNext = Integer.MAX_VALUE;
    }

    @Override
    public void inAct(Patient patient) {
        patient.setEndTime(tCurrent);
        patients.add(patient);
    }

    @Override
    public  int getQuantity() {
        return patients.size();
    }

    public double getAverageTimePatientStayInHospital() {
        double avgTime = 0;

        for (Patient patient : patients) {
            avgTime += patient.getTimeSpentInSystem();
        }

        return avgTime / patients.size();
    }

    public double getAverageTimePatientStayInHospitalByType(int type) {
        if(type < 1 || type > 3) {
            throw new RuntimeException();
        }

        double avgTime = 0;
        int amount = 0;

        for (Patient patient : patients) {
            if(patient.getType() == type) {
                avgTime += patient.getTimeSpentInSystem();
                amount++;
            }
        }

        return avgTime / amount;
    }
}
