package org.example;

public class SearchNode {

    private State curState;
    private SearchNode parent;
    private double cost; // cost to get to this state
    private double hCost; // heuristic cost
    private double fCost; // f(n) cost
    private int depth;

    public SearchNode(State s) {
        curState = s;
        parent = null;
        cost = 0;
        hCost = 0;
        fCost = 0;
        depth = 0;
    }

    public SearchNode(SearchNode prev, State s, double c, double h, int depth) {
        parent = prev;
        curState = s;
        cost = c;
        hCost = h;
        fCost = cost + hCost;
        depth = 0;
    }
    public SearchNode(SearchNode prev, State s, double c, double h) {
        parent = prev;
        curState = s;
        cost = c;
        hCost = h;
        fCost = cost + hCost;
        depth = 0;
    }

    public State getCurState() {
        return curState;
    }

    public SearchNode getParent() {
        return parent;
    }
    public double getCost() {
        return cost;
    }

    public double getHCost() {
        return hCost;
    }

    public double getFCost() {
        return fCost;
    }

    public void setFCost(double fCost) {
        this.fCost = fCost;
    }
    public int getDepth() {return depth;}
}