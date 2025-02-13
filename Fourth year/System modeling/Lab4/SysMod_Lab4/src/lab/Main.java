package lab;

import java.util.ArrayList;
import java.util.List;


public class Main {
    public static int SIM_TIME = 1000;
    public static int DELAY = 1;

    public static int ITERATIONS = 2;


    public static void main(String[] args) {

//        model1(SIMUL_TIME, ITERETIONS, 50, DELAY);
//        model2(SIMUL_TIME, ITERETIONS, 50, DELAY);
         for (int N = 100; N < 101; N+=100) {
             model1(SIM_TIME, ITERATIONS, N, DELAY);
             model2(SIM_TIME, ITERATIONS, N, DELAY);
         }
    }

    public static long model1(double simulationTime, int iterations, int N, int delay) {
        long avrgSimulTime1 = 0;

        for (int oi = 1; oi < iterations; oi++) {
            Element.resetNextId();
            Create creator = new Create("CREATOR", delay);

            Process prevProcessor = new Process("PROCESSOR_(1)", delay);
            creator.setNextElement(prevProcessor);

            ArrayList<Process> listOfProcesses = new ArrayList<>();
            listOfProcesses.add(prevProcessor);

            for (int i = 1; i < N; i++) {
                Process process = new Process("PROCESSOR_Main(1)_" + i, delay);
                listOfProcesses.add(process);
                prevProcessor.addElement(new Pair(process, 1));

                prevProcessor = process;
            }
            Model model = new Model(new ArrayList<>(){{
                add(creator);
                addAll(listOfProcesses);
            }});
            avrgSimulTime1 += measureTime(() -> model.simulate(simulationTime));
        }

        System.out.println("N = " + N);
        System.out.println("Simulation Time for Model 1: " + (avrgSimulTime1 /(double)iterations));
        return avrgSimulTime1;
    }

    public static long model2(double simulationTime, int iterations, int N, int delay) {
        long avrgSimulTime2 = 0;

        for (int oi = 1; oi < iterations; oi++) {
            Element.resetNextId();
            Create creator = new Create("CREATOR", delay);

            Process prevProcessor = new Process("PROCESSOR_(2)", delay);
            creator.setNextElement(prevProcessor);

            ArrayList<Process> listOfProcesses = new ArrayList<>();
            listOfProcesses.add(prevProcessor);

            for (int i = 1; i < N; i++) {
                Process process = new Process("PROCESSOR_Main(2)_" + i, delay);
                listOfProcesses.add(process);
                prevProcessor.addElement(new Pair(process, 1));

                prevProcessor = process;
            }

            for (int i = 0; i < listOfProcesses.size(); i++) {
                Process process = listOfProcesses.get(i);

                for (int j = i+1; j < listOfProcesses.size(); j++) {
                    process.addElement(new Pair(listOfProcesses.get(j), 1));
                }
            }

            Model model = new Model(new ArrayList<>(){{
                add(creator);
                addAll(listOfProcesses);
            }});
            avrgSimulTime2 += measureTime(() -> model.simulate(simulationTime));
        }

        System.out.println("Simulation Time for Model 2: " + (avrgSimulTime2 /(double)iterations));
        System.out.println("*******************************************");
        return avrgSimulTime2;
    }


    private static long measureTime(Runnable runnable) {
        long startTime = System.currentTimeMillis();
        runnable.run();
        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }


}