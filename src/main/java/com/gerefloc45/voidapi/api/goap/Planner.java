package com.gerefloc45.voidapi.api.goap;

import com.gerefloc45.voidapi.api.BehaviorContext;

import java.util.*;

/**
 * GOAP planner that uses A* algorithm to find optimal action sequences.
 * 
 * <p>The planner searches through possible action sequences to find
 * the lowest-cost path from the current world state to a goal state.
 * 
 * <p><b>Example:</b>
 * <pre>{@code
 * Planner planner = new Planner();
 * List<Action> availableActions = Arrays.asList(
 *     new GetWeaponAction(),
 *     new MoveToEnemyAction(),
 *     new AttackEnemyAction()
 * );
 * 
 * WorldState currentState = new WorldState();
 * currentState.set("hasWeapon", false);
 * currentState.set("nearEnemy", false);
 * 
 * Goal goal = new Goal("KillEnemy", goalState);
 * Plan plan = planner.plan(context, currentState, goal, availableActions);
 * }</pre>
 * 
 * @since 0.4.0
 */
public class Planner {
    private static final int MAX_NODES = 1000; // Prevent infinite loops
    
    /**
     * Creates a new planner.
     */
    public Planner() {
    }
    
    /**
     * Plans a sequence of actions to achieve a goal.
     * 
     * @param context The behavior context
     * @param currentState The current world state
     * @param goal The goal to achieve
     * @param availableActions Available actions to use
     * @return A plan, or null if no plan found
     */
    public Plan plan(BehaviorContext context, WorldState currentState, Goal goal, List<Action> availableActions) {
        if (goal.isSatisfied(currentState)) {
            // Goal already satisfied
            return new Plan(new ArrayList<>(), 0.0f);
        }
        
        // A* search
        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingDouble(Node::getF));
        Map<WorldState, Node> openMap = new HashMap<>();
        Set<WorldState> closedSet = new HashSet<>();
        
        Node startNode = new Node(currentState, null, null, 0.0f, 
                                   heuristic(currentState, goal.getDesiredState()));
        openSet.add(startNode);
        openMap.put(currentState, startNode);
        
        int iterations = 0;
        
        while (!openSet.isEmpty() && iterations < MAX_NODES) {
            iterations++;
            
            Node current = openSet.poll();
            openMap.remove(current.state);
            
            // Check if we reached the goal
            if (goal.isSatisfied(current.state)) {
                return reconstructPlan(current);
            }
            
            closedSet.add(current.state);
            
            // Expand neighbors
            for (Action action : availableActions) {
                if (!action.isApplicable(current.state)) {
                    continue;
                }
                
                // Simulate action
                WorldState newState = current.state.copy();
                action.applyEffects(newState);
                
                if (closedSet.contains(newState)) {
                    continue;
                }
                
                float actionCost = action.getProceduralCost(context, current.state);
                float newG = current.g + actionCost;
                float newH = heuristic(newState, goal.getDesiredState());
                
                Node existingNode = openMap.get(newState);
                if (existingNode != null) {
                    if (newG < existingNode.g) {
                        // Found better path
                        openSet.remove(existingNode);
                        openMap.remove(newState);
                    } else {
                        continue;
                    }
                }
                
                Node newNode = new Node(newState, current, action, newG, newH);
                openSet.add(newNode);
                openMap.put(newState, newNode);
            }
        }
        
        // No plan found
        return null;
    }
    
    /**
     * Heuristic function for A* (number of unsatisfied conditions).
     * 
     * @param current The current state
     * @param goal The goal state
     * @return The heuristic value
     */
    private float heuristic(WorldState current, WorldState goal) {
        return goal.countDifferences(current);
    }
    
    /**
     * Reconstructs the plan from the goal node.
     * 
     * @param goalNode The node representing the goal
     * @return The complete plan
     */
    private Plan reconstructPlan(Node goalNode) {
        List<Action> actions = new ArrayList<>();
        float totalCost = goalNode.g;
        
        Node current = goalNode;
        while (current.parent != null) {
            actions.add(current.action);
            current = current.parent;
        }
        
        Collections.reverse(actions);
        return new Plan(actions, totalCost);
    }
    
    /**
     * Node in the A* search tree.
     */
    private static class Node {
        final WorldState state;
        final Node parent;
        final Action action;
        final float g; // Cost from start
        final float h; // Heuristic to goal
        
        Node(WorldState state, Node parent, Action action, float g, float h) {
            this.state = state;
            this.parent = parent;
            this.action = action;
            this.g = g;
            this.h = h;
        }
        
        float getF() {
            return g + h;
        }
    }
}
