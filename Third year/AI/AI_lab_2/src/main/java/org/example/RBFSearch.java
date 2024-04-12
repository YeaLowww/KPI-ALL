package org.example;
import java.util.*;

public class RBFSearch {
    private static int generatedStatesCount = 0;
    private static int storedStatesCount = 0;
    private static long startTimeRBFS;
    public static long search(int[] board) {
        startTimeRBFS = System.nanoTime(); // Record the start time
        SearchNode root = new SearchNode(new EightQueensState(board));
        Stack<SearchNode> s = new Stack<>();
        s.add(root);

        SearchNode tempNode = s.pop();
        rbfs(tempNode, Integer.MAX_VALUE);
        return System.currentTimeMillis(); // Повертаємо час завершення алгоритму
    }

    private static double rbfs(SearchNode node, double fLimit) {
        if (node.getCurState().isGoal()) {
            success(node);
        }

        ArrayList<State> tempSuccessors = node.getCurState()
                .genSuccessors();
        ArrayList<SearchNode> nodeSuccessors = new ArrayList<>();

        for (State tempSuccessor : tempSuccessors) {
            generatedStatesCount++;
            SearchNode checkedNode;

            checkedNode = new SearchNode(node,
                    tempSuccessor,
                    node.getCost() + tempSuccessor.findCost(),
                    ((EightQueensState) tempSuccessor).findCost());


            if (!checkRepeats(checkedNode)) {
                nodeSuccessors.add(checkedNode);
                storedStatesCount++;
            }
        }

        if (nodeSuccessors.size() == 0) {
            return Integer.MAX_VALUE;
        }

        nodeSuccessors.sort((o1, o2) -> (int) (o1.getFCost() - o2.getFCost()));
        SearchNode lowestNode = nodeSuccessors.get(0);

        while (lowestNode.getFCost() <= fLimit && lowestNode.getFCost() < Integer.MAX_VALUE) {

            double newFLimit;
            if (nodeSuccessors.size() > 1) {
                SearchNode alternative = nodeSuccessors.get(1);
                newFLimit = Math.min(fLimit, alternative.getFCost());
            } else {
                newFLimit = fLimit;
            }
            double newFCost = rbfs(lowestNode, newFLimit);
            lowestNode.setFCost(newFCost);

            nodeSuccessors.sort((o1, o2) -> (int) (o1.getFCost() - o2.getFCost()));
            lowestNode = nodeSuccessors.get(0);
        }

        return lowestNode.getFCost();
    }

    private static boolean checkRepeats(SearchNode n) {
        boolean retValue = false;
        SearchNode checkNode = n;

        // While n's parent isn't null, check to see if it's equal to the node
        // we're looking for.
        while (n.getParent() != null && !retValue) {
            if (n.getParent().getCurState().equals(checkNode.getCurState())) {
                retValue = true;
            }
            n = n.getParent();
        }

        return retValue;
    }

    private static void success(SearchNode node) {
        Stack<SearchNode> solutionPath = new Stack<>();
        solutionPath.push(node);
        node = node.getParent();

        while (node.getParent() != null) {
            solutionPath.push(node);
            node = node.getParent();
        }
        solutionPath.push(node);

        int loopSize = solutionPath.size();

        for (int i = 0; i < loopSize; i++) {
            node = solutionPath.pop();
            node.getCurState().printState();
            System.out.println();
            System.out.println();
        }
        System.out.println("The cost was: " + node.getCost());
        System.out.println("Generated states count: " + generatedStatesCount);
        System.out.println("Stored states count: " + storedStatesCount);
// Додатковий код для вимірювання часу
        long endTimeRBFS = System.nanoTime();
        System.out.println("Time taken to RBFS: " + (endTimeRBFS - startTimeRBFS)/ 1_000_000 + "ms");
        System.exit(0);
    }
}

