import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

class Client {
    private final int arrivalTime;
    private final int serviceTimeOperator; // Час обслоператором
    private final int serviceTimeCashier; // Час обслкасиром

    public Client(int arrivalTime, int serviceTimeOperator, int serviceTimeCashier) {
        this.arrivalTime = arrivalTime;
        this.serviceTimeOperator = serviceTimeOperator;
        this.serviceTimeCashier = serviceTimeCashier;
    }
    public int getArrivalTime() {
        return arrivalTime;
    }
    public int getServiceTimeOperator() {
        return serviceTimeOperator;
    }
    public int getServiceTimeCashier() {
        return serviceTimeCashier;
    }
}

public class Main {
    private static final int MAX_CLIENTS = 10; // Максимальна кількість клієнтів в банку
    private static final int SIMULATION_TIME = 120; // Час моделювання в хвилинах
    private static final Random random = new Random();
    private static Queue<Client> clientsQueue = new LinkedList<>();
    private static int totalServiceTime = 0; // Загальний час обслуговування
    private static int totalClientsServed = 0; // Загальна кількість обслужених клієнтів

    public static void main(String[] args) {
        simulate();
        double averageServiceTime = (double) totalServiceTime / totalClientsServed;
        System.out.printf("Середній час обслуговування одного клієнта: %.2f хвилин%n", averageServiceTime);
    }

    private static void simulate() {
        for (int currentTime = 0; currentTime < SIMULATION_TIME; currentTime++) {
            // Кожні 3 хв
            if (currentTime % 3 == 0) {
                if (clientsQueue.size() < MAX_CLIENTS) {
                    int serviceTimeOperator = 2 + random.nextInt(9); // від 2 до 10
                    int serviceTimeCashier = 2 + random.nextInt(3); // від 2 до 4
                    clientsQueue.add(new Client(currentTime, serviceTimeOperator, serviceTimeCashier));
                } else {
                    System.out.println("Клієнт відмовився від обслуговування: у відділенні більше 10 клієнтів.");
                }
            }

            // Обслуговування клієнтів
            if (!clientsQueue.isEmpty()) {
                Client currentClient = clientsQueue.poll();
                int waitTime = currentTime - currentClient.getArrivalTime();
                int operatorServiceTime = currentClient.getServiceTimeOperator();
                int cashierServiceTime = currentClient.getServiceTimeCashier();

                // Час оператором
                currentTime += operatorServiceTime;
                // Час касиром
                currentTime += cashierServiceTime;

                //
                totalServiceTime += (waitTime + operatorServiceTime + cashierServiceTime);
                totalClientsServed++;
            }
        }
    }
}
