package org.example;

import java.util.ArrayList;
import java.util.Stack;

public class DFSearch
{
    private static int generatedStatesCount;
    private static int storedStatesCount;
    private static long startTimeDFS;
    public static void search(int[] board, int maxDepth)
    {
        startTimeDFS = System.nanoTime();
        SearchNode root = new SearchNode(new EightQueensState(board));
        Stack<SearchNode> stack = new Stack<SearchNode>();

        stack.add(root);
        performSearch(stack, maxDepth);
    }

    /*
     * Helper method to check to see if a SearchNode has already been evaluated.
     * Returns true if it has, false if it hasn't.
     */
    private static boolean checkRepeats(SearchNode n)
    {
        boolean retValue = false;
        SearchNode checkNode = n;

        // While n's parent isn't null, check to see if it's equal to the node
        // we're looking for.
        while (n.getParent() != null && !retValue)
        {
            if (n.getParent().getCurState().equals(checkNode.getCurState()))
            {
                retValue = true;
            }
            n = n.getParent();
        }

        return retValue;
    }

    public static void performSearch(Stack<SearchNode> s, int maxDepth)
    {
        Runtime runtime = Runtime.getRuntime();
        int searchCount = 1; // counter for number of iterations

        while (!s.isEmpty() ) // while the queue is not empty
        {
            long usedMemory = runtime.totalMemory() - runtime.freeMemory();
            if (usedMemory > 512 * 1024 * 1024) {
                System.out.println("Memory limit exceeded. Stopping the search.");
                return;
            }
            SearchNode tempNode = s.pop();

            // if tempNode is not the goal state
            if (!tempNode.getCurState().isGoal()&& tempNode.getDepth() < maxDepth)
            {
                // generate tempNode's immediate successors
                ArrayList<State> tempSuccessors = tempNode.getCurState()
                        .genSuccessors();

                /*
                 * Loop through the successors, wrap them in a SearchNode, check
                 * if they've already been evaluated, and if not, add them to
                 * the queue
                 */
                for (int i = 0; i < tempSuccessors.size(); i++)
                {
                    // second parameter here adds the cost of the new node to
                    // the current cost total in the SearchNode
                    //SearchNode newNode = new SearchNode(tempNode, tempSuccessors.get(i), tempNode.getCost() + tempSuccessors.get(i).findCost(), 0);
                    SearchNode newNode = new SearchNode(tempNode, tempSuccessors.get(i), tempNode.getCost() + tempSuccessors.get(i).findCost(), 0, tempNode.getDepth() + 1);

                    generatedStatesCount++;
                    if (!checkRepeats(newNode))
                    {
                        s.add(newNode);
                    }
                }
                storedStatesCount = Math.max(storedStatesCount, s.size());
                searchCount++;
            }
            else
            // The goal state has been found. Print the path it took to get to
            // it.
            {
                // Use a stack to track the path from the starting state to the
                // goal state
                Stack<SearchNode> solutionPath = new Stack<SearchNode>();
                solutionPath.push(tempNode);
                tempNode = tempNode.getParent();

                while (tempNode.getParent() != null)
                {
                    solutionPath.push(tempNode);
                    tempNode = tempNode.getParent();
                }
                solutionPath.push(tempNode);

                // The size of the stack before looping through and emptying it.
                int loopSize = solutionPath.size();

                for (int i = 0; i < loopSize; i++)
                {
                    tempNode = solutionPath.pop();
                    tempNode.getCurState().printState();
                    System.out.println();
                    System.out.println();
                }
                System.out.println("The cost was: " + tempNode.getCost());
                System.out.println("Generated states count: " + generatedStatesCount);
                System.out.println("Stored states count: " + storedStatesCount);
                // Додатковий код для вимірювання часу
                long endTimeDFS = System.nanoTime();
                System.out.println("Time taken to DFS: " + (endTimeDFS - startTimeDFS)/ 1_000_000 + "ms");
                System.exit(0);

            }
        }

        // This should never happen with our current puzzles.
        System.out.println("Error! No solution found!");
    }
}