package com.gerefloc45.voidapi.api.goap;

/**
 * Represents a goal that an entity wants to achieve using GOAP.
 * 
 * <p>A goal defines a desired world state and has a priority
 * that determines its importance relative to other goals.
 * 
 * <p><b>Example:</b>
 * <pre>{@code
 * // Create a goal to kill an enemy
 * WorldState desiredState = new WorldState();
 * desiredState.set("enemyAlive", false);
 * Goal killEnemy = new Goal("KillEnemy", desiredState, 10.0f);
 * }</pre>
 * 
 * @since 0.4.0
 */
public class Goal {
    private final String name;
    private final WorldState desiredState;
    private float priority;
    
    /**
     * Creates a new goal.
     * 
     * @param name The goal name (for debugging)
     * @param desiredState The desired world state
     * @param priority The goal priority (higher = more important)
     */
    public Goal(String name, WorldState desiredState, float priority) {
        this.name = name;
        this.desiredState = desiredState;
        this.priority = priority;
    }
    
    /**
     * Creates a new goal with default priority.
     * 
     * @param name The goal name
     * @param desiredState The desired world state
     */
    public Goal(String name, WorldState desiredState) {
        this(name, desiredState, 1.0f);
    }
    
    /**
     * Gets the goal name.
     * 
     * @return The goal name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Gets the desired world state.
     * 
     * @return The desired state
     */
    public WorldState getDesiredState() {
        return desiredState;
    }
    
    /**
     * Gets the goal priority.
     * 
     * @return The priority value
     */
    public float getPriority() {
        return priority;
    }
    
    /**
     * Sets the goal priority.
     * 
     * @param priority The new priority
     */
    public void setPriority(float priority) {
        this.priority = priority;
    }
    
    /**
     * Checks if this goal is satisfied in the given world state.
     * 
     * @param currentState The current world state
     * @return True if the goal is achieved
     */
    public boolean isSatisfied(WorldState currentState) {
        return currentState.satisfies(desiredState);
    }
    
    /**
     * Gets the relevance of this goal given the current state.
     * 
     * <p>By default, returns the priority. Can be overridden
     * to implement context-dependent relevance.
     * 
     * @param currentState The current world state
     * @return The relevance score
     */
    public float getRelevance(WorldState currentState) {
        return priority;
    }
    
    @Override
    public String toString() {
        return "Goal{" +
                "name='" + name + '\'' +
                ", priority=" + priority +
                ", desiredState=" + desiredState +
                '}';
    }
}
