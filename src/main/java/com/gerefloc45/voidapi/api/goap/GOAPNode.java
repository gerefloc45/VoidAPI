package com.gerefloc45.voidapi.api.goap;

import com.gerefloc45.voidapi.api.BehaviorContext;
import com.gerefloc45.voidapi.api.BehaviorNode;

import java.util.List;
import java.util.function.Function;

/**
 * Behavior tree node that integrates GOAP planning.
 * 
 * <p>This node automatically plans and executes action sequences
 * to achieve a goal. It handles replanning when actions fail or
 * the world state changes significantly.
 * 
 * <p><b>Example:</b>
 * <pre>{@code
 * // Define actions
 * List<Action> actions = Arrays.asList(
 *     new GetWeaponAction(),
 *     new MoveToEnemyAction(),
 *     new AttackEnemyAction()
 * );
 * 
 * // Define goal
 * WorldState goalState = new WorldState();
 * goalState.set("enemyAlive", false);
 * Goal goal = new Goal("KillEnemy", goalState, 10.0f);
 * 
 * // Create world state provider
 * Function<BehaviorContext, WorldState> stateProvider = ctx -> {
 *     WorldState state = new WorldState();
 *     state.set("hasWeapon", hasWeapon(ctx));
 *     state.set("nearEnemy", isNearEnemy(ctx));
 *     return state;
 * };
 * 
 * // Create GOAP node
 * GOAPNode goapNode = new GOAPNode(goal, actions, stateProvider);
 * }</pre>
 * 
 * @since 0.4.0
 */
public class GOAPNode extends BehaviorNode {
    private final Goal goal;
    private final List<Action> availableActions;
    private final Function<BehaviorContext, WorldState> worldStateProvider;
    private final Planner planner;
    private final PlanExecutor executor;
    private final int replanInterval;
    
    private int ticksSinceLastPlan;
    private WorldState lastWorldState;
    
    /**
     * Creates a new GOAP node.
     * 
     * @param goal The goal to achieve
     * @param availableActions Available actions for planning
     * @param worldStateProvider Function to get current world state
     * @param replanInterval Ticks between replanning checks
     */
    public GOAPNode(Goal goal, List<Action> availableActions, 
                    Function<BehaviorContext, WorldState> worldStateProvider,
                    int replanInterval) {
        this.goal = goal;
        this.availableActions = availableActions;
        this.worldStateProvider = worldStateProvider;
        this.replanInterval = replanInterval;
        this.planner = new Planner();
        this.executor = new PlanExecutor();
        this.ticksSinceLastPlan = 0;
        this.lastWorldState = null;
    }
    
    /**
     * Creates a new GOAP node with default replan interval.
     * 
     * @param goal The goal to achieve
     * @param availableActions Available actions for planning
     * @param worldStateProvider Function to get current world state
     */
    public GOAPNode(Goal goal, List<Action> availableActions,
                    Function<BehaviorContext, WorldState> worldStateProvider) {
        this(goal, availableActions, worldStateProvider, 20); // Replan every second by default
    }
    
    @Override
    public Status execute(BehaviorContext context) {
        WorldState currentState = worldStateProvider.apply(context);
        
        // Check if goal is already satisfied
        if (goal.isSatisfied(currentState)) {
            return Status.SUCCESS;
        }
        
        // Check if we need to (re)plan
        boolean needsPlanning = !executor.hasPlan() || 
                                ticksSinceLastPlan >= replanInterval ||
                                shouldReplan(currentState);
        
        if (needsPlanning) {
            Plan newPlan = planner.plan(context, currentState, goal, availableActions);
            
            if (newPlan == null) {
                // No plan found - goal is unreachable
                return Status.FAILURE;
            }
            
            executor.cancel(context);
            executor.setPlan(newPlan);
            ticksSinceLastPlan = 0;
            lastWorldState = currentState.copy();
        }
        
        // Execute current plan
        Status executionStatus = executor.execute(context);
        
        if (executionStatus == Status.FAILURE) {
            // Action failed - try replanning
            Plan newPlan = planner.plan(context, currentState, goal, availableActions);
            
            if (newPlan == null) {
                return Status.FAILURE;
            }
            
            executor.setPlan(newPlan);
            ticksSinceLastPlan = 0;
            lastWorldState = currentState.copy();
            return Status.RUNNING;
        }
        
        ticksSinceLastPlan++;
        return executionStatus;
    }
    
    /**
     * Determines if replanning is needed based on world state changes.
     * 
     * @param currentState The current world state
     * @return True if replanning is recommended
     */
    private boolean shouldReplan(WorldState currentState) {
        if (lastWorldState == null) {
            return true;
        }
        
        // Check if significant state changes occurred
        // This is a simple check - can be made more sophisticated
        for (String key : lastWorldState.keys()) {
            Object oldValue = lastWorldState.get(key);
            Object newValue = currentState.get(key);
            
            if (!java.util.Objects.equals(oldValue, newValue)) {
                return true;
            }
        }
        
        return false;
    }
    
    @Override
    public void onStart(BehaviorContext context) {
        super.onStart(context);
        ticksSinceLastPlan = replanInterval; // Force initial planning
    }
    
    @Override
    public void onEnd(BehaviorContext context, Status status) {
        executor.cancel(context);
        super.onEnd(context, status);
    }
    
    /**
     * Gets the current goal.
     * 
     * @return The goal
     */
    public Goal getGoal() {
        return goal;
    }
    
    /**
     * Gets the planner.
     * 
     * @return The planner instance
     */
    public Planner getPlanner() {
        return planner;
    }
    
    /**
     * Gets the executor.
     * 
     * @return The executor instance
     */
    public PlanExecutor getExecutor() {
        return executor;
    }
    
    /**
     * Gets the current plan.
     * 
     * @return The active plan, or null
     */
    public Plan getCurrentPlan() {
        return executor.getPlan();
    }
    
    @Override
    public String toString() {
        return "GOAPNode{" +
                "goal=" + goal.getName() +
                ", plan=" + executor.getPlan() +
                '}';
    }
}
