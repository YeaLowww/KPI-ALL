package task2;

import  task2.utils.Consts;

import java.util.ArrayList;
import java.util.HashMap;

public class SimModel {
    public static void main(String[] args) {
        double TIME = 10000.0;

        Create c = new Create(15, 1);
        c.setName("Creator");
        c.setDistribution(Consts.DistributionType.exponential);

        Process reception = new Process(0, 2, Integer.MAX_VALUE);
        reception.setName("Reception");

        Process hospitalRoom = new Process(3, 3, Integer.MAX_VALUE);
        hospitalRoom.setName("Walk to hospital room"); //для супроводу пацієнтів у палати
        hospitalRoom.setMaxqueue(Integer.MAX_VALUE);
        hospitalRoom.setDistribution(Consts.DistributionType.uniform);
        hospitalRoom.setDelayDev(8);

        Process labRegistry = new Process(4.5, 1, Integer.MAX_VALUE);
        labRegistry.setName("Wait for registration to lab");
        labRegistry.setMaxqueue(Integer.MAX_VALUE);
        labRegistry.setDistribution(Consts.DistributionType.erlang);
        labRegistry.k = 3;

        Process lab = new Process(4, 2, Integer.MAX_VALUE);
        lab.setName("Take analyses");
        lab.setMaxqueue(Integer.MAX_VALUE);
        lab.setDistribution(Consts.DistributionType.erlang);
        lab.k = 2;

        Dispose d = new Dispose();
        d.setName("Disposer");

        c.setNextElement(reception);

        HashMap<Integer, Element> receptionNextElements = new HashMap<>();
        receptionNextElements.put(Consts.PatientTypes.toHospitalRoom, hospitalRoom);//тип 1 на лікування
        receptionNextElements.put(Consts.PatientTypes.toLab2, labRegistry); //Пацієнти, які потребують лабораторного обстеження
        receptionNextElements.put(Consts.PatientTypes.toLab3, labRegistry); //2 3 to labRegistry
        reception.setNextTypedElementArray(receptionNextElements);

        labRegistry.setNextElement(lab);

        HashMap<Integer, Element> labNextElements = new HashMap<>();
        labNextElements.put(Consts.PatientTypes.toHospitalRoom, reception);//Після реєстрації пацієнти переходять до процесу здачі аналізів.
        labNextElements.put(Consts.PatientTypes.toLab3, d); //Після здачі аналізів
        lab.setNextTypedElementArray(labNextElements);

        hospitalRoom.setNextElement(d);



        ArrayList<Element> list = new ArrayList<>();
        list.add(c);
        list.add(reception);
        list.add(hospitalRoom);
        list.add(labRegistry);
        list.add(lab);
        list.add(d);
        new Model(list).simulate(TIME);
    }
}
