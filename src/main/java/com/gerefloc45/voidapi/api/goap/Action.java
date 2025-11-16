package com.gerefloc45.voidapi.api.goap;

import com.gerefloc45.voidapi.api.BehaviorContext;

/**
 * Represents an action that can be performed in GOAP planning.
 * 
 * <p>An action has:
 * <ul>
 *   <li><b>Preconditions</b> - World state requirements to execute</li>
 *   <li><b>Effects</b> - Changes to world state after execution</li>
 *   <li><b>Cost</b> - Relative expense of performing the action</li>
 * </ul>
 * 
 * <p><b>Example:</b>
 * <pre>{@code
 * public class AttackEnemyAction extends Action {
 *     public AttackEnemyAction() {
 *         super("AttackEnemy", 1.0f);
 *         
 *         // Preconditions: must have weapon and be near enemy
 *         preconditions.set("hasWeapon", true);
 *         preconditions.set("nearEnemy", true);
 *         
 *         // Effects: enemy is no longer alive
 *         effects.set("enemyAlive", false);
 *     }
 *     
 *     @Override
 *     public boolean canRun(BehaviorContext context) {
 *         return context.getEntity().getTarget() != null;
 *     }
 *     
 *     @Override
 *     public boolean execute(BehaviorContext context) {
 *         // Attack logic here
 *         return true;
 *     }
 * }
 * }</pre>
 * 
 * @since 0.4.0
 */
public abstract class Action {
    private final String name;
    private float cost;
    protected final WorldState preconditions;
    protected final WorldState effects;
    private boolean isRunning;
    
    /**
     * Creates a new action.
     * 
     * @param name The action name (for debugging)
     * @param cost The action cost (lower = preferred)
     */
    public Action(String name, float cost) {
        this.name = name;
        this.cost = cost;
        this.preconditions = new WorldState();
        this.effects = new WorldState();
        this.isRunning = false;
    }
    
    /**
     * Creates a new action with default cost.
     * 
     * @param name The action name
     */
    public Action(String name) {
        this(name, 1.0f);
    }
    
    /**
     * Gets the action name.
     * 
     * @return The action name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Gets the action cost.
     * 
     * @return The cost value
     */
    public float getCost() {
        return cost;
    }
    
    /**
     * Sets the action cost.
     * 
     * @param cost The new cost
     */
    public void setCost(float cost) {
        this.cost = cost;
    }
    
    /**
     * Gets the preconditions for this action.
     * 
     * @return The preconditions state
     */
    public WorldState getPreconditions() {
        return preconditions;
    }
    
    /**
     * Gets the effects of this action.
     * 
     * @return The effects state
     */
    public WorldState getEffects() {
        return effects;
    }
    
    /**
     * Checks if the action is currently running.
     * 
     * @return True if running
     */
    public boolean isRunning() {
        return isRunning;
    }
    
    /**
     * Checks if this action can be run in the current context.
     * 
     * <p>This is checked before execution to validate runtime
     * conditions beyond the world state.
     * 
     * @param context The behavior context
     * @return True if the action can run
     */
    public abstract boolean canRun(BehaviorContext context);
    
    /**
     * Executes the action.
     * 
     * <p>Implementations should perform the actual action logic here.
     * This may take multiple ticks - return false while still executing.
     * 
     * @param context The behavior context
     * @return True if execution completed successfully, false if still running or failed
     */
    public abstract boolean execute(BehaviorContext context);
    
    /**
     * Called when the action starts executing.
     * 
     * <p>Override to implement initialization logic.
     * 
     * @param context The behavior context
     */
    public void onStart(BehaviorContext context) {
        isRunning = true;
    }
    
    /**
     * Called when the action completes or is interrupted.
     * 
     * <p>Override to implement cleanup logic.
     * 
     * @param context The behavior context
     * @param success True if completed successfully
     */
    public void onEnd(BehaviorContext context, boolean success) {
        isRunning = false;
    }
    
    /**
     * Checks if the action is applicable in the given state.
     * 
     * <p>An action is applicable if its preconditions are satisfied.
     * 
     * @param currentState The current world state
     * @return True if preconditions are met
     */
    public boolean isApplicable(WorldState currentState) {
        return currentState.satisfies(preconditions);
    }
    
    /**
     * Applies the action's effects to a world state.
     * 
     * <p>This is used during planning to simulate action execution.
     * 
     * @param state The state to modify
     */
    public void applyEffects(WorldState state) {
        state.apply(effects);
    }
    
    /**
     * Gets the procedural cost of this action in the current context.
     * 
     * <p>By default, returns the base cost. Can be overridden to
     * implement context-dependent costs (e.g., distance to target).
     * 
     * @param context The behavior context
     * @param currentState The current world state
     * @return The procedural cost
     */
    public float getProceduralCost(BehaviorContext context, WorldState currentState) {
        return cost;
    }
    
    @Override
    public String toString() {
        return "Action{" +
                "name='" + name + '\'' +
                ", cost=" + cost +
                ", preconditions=" + preconditions +
                ", effects=" + effects +
                '}';
    }
}
