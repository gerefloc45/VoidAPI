package com.gerefloc45.voidapi.api.goap;

import com.gerefloc45.voidapi.api.Behavior;
import com.gerefloc45.voidapi.api.BehaviorContext;

/**
 * Executes a GOAP plan action by action.
 * 
 * <p>The executor runs each action in sequence, advancing when
 * each action completes. If an action fails or becomes invalid,
 * the plan is marked as failed and needs replanning.
 * 
 * <p><b>Example:</b>
 * <pre>{@code
 * PlanExecutor executor = new PlanExecutor(plan);
 * 
 * // In your behavior tree tick:
 * Behavior.Status status = executor.execute(context);
 * if (status == Behavior.Status.FAILURE) {
 *     // Replan needed
 * }
 * }</pre>
 * 
 * @since 0.4.0
 */
public class PlanExecutor {
    private Plan plan;
    private Action currentAction;
    private boolean currentActionStarted;
    
    /**
     * Creates a new plan executor.
     * 
     * @param plan The plan to execute
     */
    public PlanExecutor(Plan plan) {
        this.plan = plan;
        this.currentAction = null;
        this.currentActionStarted = false;
    }
    
    /**
     * Creates an empty executor with no plan.
     */
    public PlanExecutor() {
        this(null);
    }
    
    /**
     * Sets a new plan to execute.
     * 
     * <p>This resets any current execution state.
     * 
     * @param plan The new plan
     */
    public void setPlan(Plan plan) {
        if (currentAction != null && currentActionStarted) {
            // Clean up current action if needed
            currentAction = null;
        }
        this.plan = plan;
        this.currentActionStarted = false;
    }
    
    /**
     * Gets the current plan.
     * 
     * @return The plan being executed
     */
    public Plan getPlan() {
        return plan;
    }
    
    /**
     * Executes the current action in the plan.
     * 
     * @param context The behavior context
     * @return The execution status
     */
    public Behavior.Status execute(BehaviorContext context) {
        if (plan == null || plan.isEmpty()) {
            return Behavior.Status.FAILURE;
        }
        
        if (plan.isComplete()) {
            return Behavior.Status.SUCCESS;
        }
        
        // Get current action
        if (currentAction == null || currentAction != plan.getCurrentAction()) {
            currentAction = plan.getCurrentAction();
            currentActionStarted = false;
        }
        
        if (currentAction == null) {
            return Behavior.Status.FAILURE;
        }
        
        // Check if action can run
        if (!currentAction.canRun(context)) {
            // Action became invalid - plan needs updating
            if (currentActionStarted) {
                currentAction.onEnd(context, false);
                currentActionStarted = false;
            }
            return Behavior.Status.FAILURE;
        }
        
        // Start action if needed
        if (!currentActionStarted) {
            currentAction.onStart(context);
            currentActionStarted = true;
        }
        
        // Execute action
        boolean completed = currentAction.execute(context);
        
        if (completed) {
            // Action finished
            currentAction.onEnd(context, true);
            currentActionStarted = false;
            plan.advance();
            currentAction = null;
            
            // Check if plan is complete
            if (plan.isComplete()) {
                return Behavior.Status.SUCCESS;
            }
            
            // Continue to next action (return RUNNING to allow next tick)
            return Behavior.Status.RUNNING;
        }
        
        // Action still running
        return Behavior.Status.RUNNING;
    }
    
    /**
     * Cancels the current plan execution.
     * 
     * <p>Cleans up the current action if running.
     * 
     * @param context The behavior context
     */
    public void cancel(BehaviorContext context) {
        if (currentAction != null && currentActionStarted) {
            currentAction.onEnd(context, false);
            currentActionStarted = false;
        }
        currentAction = null;
        if (plan != null) {
            plan.reset();
        }
    }
    
    /**
     * Checks if there is a plan being executed.
     * 
     * @return True if a plan exists
     */
    public boolean hasPlan() {
        return plan != null && !plan.isEmpty();
    }
    
    /**
     * Checks if the executor is currently executing an action.
     * 
     * @return True if an action is running
     */
    public boolean isExecuting() {
        return currentAction != null && currentActionStarted;
    }
    
    /**
     * Gets the current action being executed.
     * 
     * @return The current action, or null
     */
    public Action getCurrentAction() {
        return currentAction;
    }
    
    @Override
    public String toString() {
        return "PlanExecutor{" +
                "plan=" + plan +
                ", currentAction=" + (currentAction != null ? currentAction.getName() : "none") +
                ", executing=" + currentActionStarted +
                '}';
    }
}
