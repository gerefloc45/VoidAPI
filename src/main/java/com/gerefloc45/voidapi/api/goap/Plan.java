package com.gerefloc45.voidapi.api.goap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a planned sequence of actions to achieve a goal.
 * 
 * <p>A plan contains an ordered list of actions and the total cost
 * of executing those actions.
 * 
 * @since 0.4.0
 */
public class Plan {
    private final List<Action> actions;
    private final float totalCost;
    private int currentActionIndex;
    
    /**
     * Creates a new plan.
     * 
     * @param actions The sequence of actions
     * @param totalCost The total cost of the plan
     */
    public Plan(List<Action> actions, float totalCost) {
        this.actions = new ArrayList<>(actions);
        this.totalCost = totalCost;
        this.currentActionIndex = 0;
    }
    
    /**
     * Gets all actions in the plan.
     * 
     * @return An unmodifiable list of actions
     */
    public List<Action> getActions() {
        return Collections.unmodifiableList(actions);
    }
    
    /**
     * Gets the total cost of the plan.
     * 
     * @return The total cost
     */
    public float getTotalCost() {
        return totalCost;
    }
    
    /**
     * Gets the current action being executed.
     * 
     * @return The current action, or null if plan is complete
     */
    public Action getCurrentAction() {
        if (currentActionIndex < actions.size()) {
            return actions.get(currentActionIndex);
        }
        return null;
    }
    
    /**
     * Advances to the next action in the plan.
     */
    public void advance() {
        currentActionIndex++;
    }
    
    /**
     * Checks if the plan is complete.
     * 
     * @return True if all actions have been executed
     */
    public boolean isComplete() {
        return currentActionIndex >= actions.size();
    }
    
    /**
     * Resets the plan to the beginning.
     */
    public void reset() {
        currentActionIndex = 0;
    }
    
    /**
     * Gets the number of actions in the plan.
     * 
     * @return The plan length
     */
    public int size() {
        return actions.size();
    }
    
    /**
     * Checks if the plan is empty.
     * 
     * @return True if there are no actions
     */
    public boolean isEmpty() {
        return actions.isEmpty();
    }
    
    /**
     * Gets the current action index.
     * 
     * @return The index of the current action
     */
    public int getCurrentActionIndex() {
        return currentActionIndex;
    }
    
    /**
     * Gets the remaining number of actions.
     * 
     * @return The number of actions left to execute
     */
    public int getRemainingActions() {
        return Math.max(0, actions.size() - currentActionIndex);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Plan{cost=" + totalCost + ", actions=[");
        for (int i = 0; i < actions.size(); i++) {
            if (i > 0) sb.append(", ");
            if (i == currentActionIndex) sb.append(">");
            sb.append(actions.get(i).getName());
        }
        sb.append("]}");
        return sb.toString();
    }
}
