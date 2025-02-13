package hospital;

public class Lab extends Process {
    private Despose despose;
    private Process walkToReception;

    public Lab(String name, int workersAmount) {
        super(name, workersAmount);
    }

    public void setDespose(Despose despose) {
        this.despose = despose;
    }
    public void setWalkToReception(Process walkToReception) {
        this.walkToReception = walkToReception;
    }

    @Override
    protected void goToNextElement(Patient patient) {
        if (patient.getType() == 3) {
            despose.inAct(patient);
        } else {
            patient.setType(1);
            walkToReception.inAct(patient);
        }
    }
}
