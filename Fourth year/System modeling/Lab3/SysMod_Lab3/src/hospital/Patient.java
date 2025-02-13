package hospital;

public class Patient {
    private double startTime, endTime;
    private int type;
    private double tNext;

    public Patient(double startTime, int type) {
        if(startTime < 0 || type < 1 || type > 3 ) {
            throw new RuntimeException("Invalid!!!");
        }

        this.startTime = startTime;
        this.type = type;
    }

    public void setType(int type) {
        this.type = type;
    }
    public void setEndTime(double endTime) {
        this.endTime = endTime;
    }
    public void setTNext(double tNext) {
        this.tNext = tNext;
    }

    public int getType() {
        return type;
    }
    public double getTimeSpentInSystem() {
        return endTime - startTime;
    }
    public double getTNext() {
        return tNext;
    }
}
