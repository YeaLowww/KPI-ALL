package org.example;
import java.util.ArrayList;
import java.util.Arrays;

public class EightQueensState implements State {

    private final int BOARD_SIZE = 8;

    private int[] curBoard;

    public EightQueensState(int[] board) {
        curBoard = board;
    }

    @Override
    public double findCost() {
        return calculateAttacks();
    }

    private double calculateAttacks() {
        int attacks = 0;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = i + 1; j < BOARD_SIZE; j++) {
                if (curBoard[i] == curBoard[j] ||
                        Math.abs(curBoard[i] - curBoard[j]) == Math.abs(i - j)) {
                    attacks++;
                }
            }
        }
        return attacks;
    }

    private int[] copyBoard(int[] state) {
        return Arrays.copyOf(state, BOARD_SIZE);
    }

    @Override
    public ArrayList<State> genSuccessors() {
        ArrayList<State> successors = new ArrayList<>();
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (i != j) {
                    int[] nextState = copyBoard(curBoard);
                    nextState[i] = j;
                    successors.add(new EightQueensState(nextState));
                }
            }
        }
        return successors;
    }

    @Override
    public boolean isGoal() {
        return calculateAttacks() == 0;
    }

    @Override
    public void printState() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                System.out.print(curBoard[i] == j ? "Q " : ". ");
            }
            System.out.println();
        }
        System.out.println();
    }

    @Override
    public boolean equals(State s) {
        return Arrays.equals(curBoard, ((EightQueensState) s).getCurBoard());
    }

    public int[] getCurBoard() {
        return curBoard;
    }
}