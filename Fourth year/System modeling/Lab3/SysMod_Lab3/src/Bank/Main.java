package Bank;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Main {

    public static int MAX_QUEUE = 3;
    public static double CASHIER1_DELAY = 0.3;
    public static double CASHIER2_DELAY = 0.3;
    public static double CREATOR_DELAY = 0.5;

    public static int TIME_SIMULATION = 100;

    public static void main(String[] args) {
        bankTask();
    }

    public static void bankTask() {
        Create creator = new Create("CREATOR", CREATOR_DELAY);
        Cashier cashier1 = new Cashier("CASHIER1", CASHIER1_DELAY, MAX_QUEUE);
        Cashier cashier2 = new Cashier("CASHIER2", CASHIER2_DELAY, MAX_QUEUE);
        Despose despose = new Despose("DESPOSE");

        creator.setPriorityCashier(cashier1);
        creator.setNonPriorityCashier(cashier2);

        cashier1.setNextElement(despose);
        cashier1.setAnotherCashier(cashier2);

        cashier2.setNextElement(despose);
        cashier2.setAnotherCashier(cashier1);

        Model model = new Model(new ArrayList<>() {{
            add(creator);
            add(cashier1);
            add(cashier2);
            add(despose);
        }});

        model.simulate(TIME_SIMULATION);

        System.out.println("1) Average load" +
                "\nCash№1: " + String.format("%.2f", cashier1.getMeanLoad() / TIME_SIMULATION) +
                "\nCash№2: " + String.format("%.2f", cashier2.getMeanLoad() / TIME_SIMULATION));

        System.out.println("2) Average client in bank: " + String.format("%.2f", (
                cashier1.getMeanLoad() + cashier1.getMeanQueue() +
                        cashier2.getMeanLoad() + cashier2.getMeanQueue()) / TIME_SIMULATION));

        System.out.println("3) Average between customers leaving the windows: " +
                String.format("%.2f", TIME_SIMULATION / (double) creator.getQuantity()));

        System.out.println("4) Average time in bank: " + String.format("%.2f", despose.getAverageTimeClientStayInBank()));

        System.out.println("5) Average client in" +
                "\nQueue №1: " + String.format("%.2f", cashier1.getMeanQueue() / TIME_SIMULATION) +
                "\nQueue №2: " + String.format("%.2f", cashier2.getMeanQueue() / TIME_SIMULATION));

        System.out.println("6) Failure: " + String.format("%.2f", (creator.getFailure() / (double) creator.getQuantity()) * 100));

        System.out.println("7) Road swap:" +
                "\nFrom №1 to №2: " + cashier1.getAmountOfSwitchesToAnotherCashier() +
                "\nFrom №2 to №1: " + cashier2.getAmountOfSwitchesToAnotherCashier());
    }
}