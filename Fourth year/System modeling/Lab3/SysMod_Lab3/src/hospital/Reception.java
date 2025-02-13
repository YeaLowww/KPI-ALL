package hospital;

public class Reception extends Process {
    private Process room;
    private Process walkToRegistry;

    public Reception(String name, int workersAmount) {
        super(name, workersAmount);
    }

    public void setRoom(Process room) {
        this.room = room;
    }
    public void setWalkToRegistry(Process walkToRegistry) {
        this.walkToRegistry = walkToRegistry;
    }
 
    @Override
    protected double getDelay(Patient patient) {
        switch (patient.getType()) {
            case 1: return 15;
            case 2: return 40;
            case 3: return 30;
        }

        throw new RuntimeException("Invalid patient");
    }
    @Override
    protected Patient getPatientFromQueue() {
        for (Patient patient : queue) {
            if (patient.getType() == 1) {
                queue.remove(patient);
                return patient;
            }
        }

        return queue.poll();
    }
    @Override
    protected void goToNextElement(Patient patient) {
        if (patient.getType() == 1) {
            room.inAct(patient);
        } else {
            walkToRegistry.inAct(patient);
        }
    }
}
