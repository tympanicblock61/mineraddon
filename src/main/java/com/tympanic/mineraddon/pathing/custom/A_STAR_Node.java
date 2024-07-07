package com.tympanic.mineraddon.pathing.custom;

/**
 * Represents a node in A* algorithm with costs for pathfinding.
 *
 * @param G_cost
 * The Distance from the starting node to this node
 *
 * @param H_cost
 * The Distance from the ending node to this node
 */
public record A_STAR_Node(int G_cost, int H_cost) {

    public A_STAR_Node {
        if (G_cost < 0 || H_cost < 0) {
            throw new IllegalArgumentException("G_cost and H_cost must be non-negative");
        }
    }

    /**
     * Calculates and returns the F_cost which is the combined G_cost and H_cost.
     *
     * @return The combined cost of G_cost and H_cost.
     */
    public int F_cost() {
        return G_cost + H_cost;
    }
}
