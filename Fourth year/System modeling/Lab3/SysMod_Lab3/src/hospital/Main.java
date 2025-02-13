package hospital;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Main {

    public static int TIME_SIMULATION = 10000;

    public static int WORKERS_RECEPTION = 2;
    public static int WORKERS_ROOM= 2;
    public static int WORKERS_REGISTRY = 3;
    public static int WORKERS_LAB = 2;

    public static void main(String[] args) {
        hospitalTask();
    }

    public static void hospitalTask() {
        Create creator = new Create("CREATOR");
        Reception reception = new Reception("RECEPTION", WORKERS_RECEPTION);
        Process room = new Process("ROOM", WORKERS_ROOM);
        Process walkToRegistry = new Process("WALK_TO_REGISTRY");
        Process registry = new Process("REGISTRY", WORKERS_REGISTRY);
        Lab lab = new Lab("LAB", WORKERS_LAB);
        Process walkToReception = new Process("WALK_TO_RECEPTION");
        Despose despose = new Despose("DESPOSE");


        creator.Exp_Distribution(15);
        room.Unif_Distribution(3, 8);
        walkToRegistry.Unif_Distribution(2, 5);
        registry.Erlang_Distribution(4.5, 3);
        lab.Erlang_Distribution(4, 2);
        walkToReception.Unif_Distribution(2, 5);

        creator.setNextElement(reception);
        reception.setWalkToRegistry(walkToRegistry);
        reception.setRoom(room);
        room.setNextElement(despose);
        walkToRegistry.setNextElement(registry);
        registry.setNextElement(lab);
        lab.setWalkToReception(walkToReception);
        lab.setDespose(despose);
        walkToReception.setNextElement(reception);

        Model model = new Model(new ArrayList<>(){{
            add(creator);
            add(reception);
            add(room);
            add(walkToRegistry);
            add(registry);
            add(lab);
            add(walkToReception);
            add(despose);
        }});

        model.simulate(TIME_SIMULATION);

        System.out.println("Time in system: " + String.format("%.2f", despose.getAverageTimePatientStayInHospital()));
        System.out.println("Type1: " + String.format("%.2f", despose.getAverageTimePatientStayInHospitalByType(1)));
        System.out.println("Type2: " + String.format("%.2f", despose.getAverageTimePatientStayInHospitalByType(2)));
        System.out.println("Type3: " + String.format("%.2f", despose.getAverageTimePatientStayInHospitalByType(3)));

        System.out.println("Arrivals interval at the laboratory: " + String.format("%.2f", TIME_SIMULATION / (double)creator.getQuantity()));
    }
}
