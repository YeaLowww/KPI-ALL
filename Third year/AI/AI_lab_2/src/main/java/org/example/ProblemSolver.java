package org.example;

import java.util.Scanner;
import java.util.Random;


public class ProblemSolver {

    public static void main(String[] args) {

        Scanner reader = new Scanner(System.in);

//        System.out.println("Enter the initial positions of the queens (example: 0 1 2 3 4 5 6 7): \n");
//
//        int[] startingStateBoard = dispatchEightQueens(reader.nextLine().split(" "));
        //int[] startingStateBoard = generateRandomBoard();
        int[] startingStateBoard = generateGoalBoard();
        //int[] startingStateBoard={6, 1, 2, 3, 5, 0, 7, 2 };
        //int[] startingStateBoard={1, 6, 4, 5, 3, 7, 0, 2};

        System.out.println("Початковий стан дошки: ");
        for (int i = 0; i < 8; i++) {
            System.out.print(startingStateBoard[i] + " ");
        }
        System.out.println();
        System.out.println("Which Algorithm ?\n" +
                "1 - dfs\n" +
                "2 - rbfs\n");

        int choice = reader.nextInt();

        switch (choice) {
            case 1:
                DFSearch.search(startingStateBoard,6);
                break;
            case 2:
                long startTimeDFS = System.nanoTime();
                RBFSearch.search(startingStateBoard);
                break;
            default:
                System.out.println("Invalid choice");
        }
    }
    public static int[] generateRandomBoard() {
        int[] board = new int[8];
        Random random = new Random();
        for (int i = 0; i < 8; i++) {
            board[i] = random.nextInt(8); // випадкове число від 0 до 7
        }
        return board;
    }
    public static int[] generateGoalBoard() {
        int[] board = new int[8];
        Random random = new Random();
        for (int i = 0; i < 8; i++) {
            board[i] = i; // кожен ферзь стоїть на своєму рядку
        }
        // Перемішати розміщення ферзів у випадковому порядку
        for (int i = 0; i < 8; i++) {
            int j = random.nextInt(8);
            int temp = board[i];
            board[i] = board[j];
            board[j] = temp;
        }
        return board;
    }


    private static int[] dispatchEightQueens(String[] a) {
        int[] initState = new int[8];
        for (int i = 0; i < a.length; i++) {
            initState[i] = Integer.parseInt(a[i]);
        }
        return initState;
    }
}
