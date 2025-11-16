package com.gerefloc45.voidapi.api.ml;

import com.gerefloc45.voidapi.api.Behavior;
import com.gerefloc45.voidapi.api.BehaviorContext;
import com.gerefloc45.voidapi.api.BehaviorNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Behavior tree node that learns from experience.
 * 
 * <p>This node wraps other behaviors and learns which ones
 * are most effective in different situations, automatically
 * selecting the best behavior over time.
 * 
 * <p><b>Features:</b>
 * <ul>
 *   <li>Automatic behavior selection</li>
 *   <li>Success/failure tracking</li>
 *   <li>Exploration vs exploitation</li>
 *   <li>Persistent learning</li>
 * </ul>
 * 
 * <p><b>Example:</b>
 * <pre>{@code
 * LearningNode learner = new LearningNode()
 *     .addBehavior("attack", attackBehavior)
 *     .addBehavior("flee", fleeBehavior)
 *     .addBehavior("hide", hideBehavior);
 * 
 * // Node automatically learns which behavior works best
 * tree.addChild(learner);
 * }</pre>
 * 
 * @since 0.5.0
 */
public class LearningNode extends BehaviorNode {
    private final List<NamedBehavior> behaviors;
    
    private NamedBehavior currentBehavior;
    private long actionStartTime;
    
    /**
     * Creates a new learning node.
     */
    public LearningNode() {
        this.behaviors = new ArrayList<>();
    }
    
    /**
     * Adds a behavior option.
     * 
     * @param name Behavior name
     * @param behavior The behavior
     * @return This node for chaining
     */
    public LearningNode addBehavior(String name, Behavior behavior) {
        behaviors.add(new NamedBehavior(name, behavior));
        return this;
    }
    
    @Override
    public void onStart(BehaviorContext context) {
        super.onStart(context);
        
        // Get or create learner for this entity
        BehaviorLearner entityLearner = getEntityLearner(context);
        
        // Select behavior using learner
        String bestAction = entityLearner.getBestAction(context);
        
        if (bestAction != null) {
            // Use learned behavior
            currentBehavior = behaviors.stream()
                .filter(b -> b.name.equals(bestAction))
                .findFirst()
                .orElse(behaviors.get(0));
        } else {
            // No learned behavior yet, use first
            currentBehavior = behaviors.isEmpty() ? null : behaviors.get(0);
        }
        
        if (currentBehavior != null) {
            entityLearner.recordAction(currentBehavior.name, context);
            actionStartTime = System.currentTimeMillis();
        }
    }
    
    @Override
    public Status execute(BehaviorContext context) {
        if (currentBehavior == null || behaviors.isEmpty()) {
            return Status.FAILURE;
        }
        
        // Execute current behavior
        Status status = currentBehavior.behavior.execute(context);
        
        // Record outcome if completed
        if (status != Status.RUNNING) {
            BehaviorLearner entityLearner = getEntityLearner(context);
            
            boolean success = (status == Status.SUCCESS);
            float reward = calculateReward(context, success);
            
            entityLearner.recordOutcome(currentBehavior.name, success, reward);
        }
        
        return status;
    }
    
    @Override
    public void onEnd(BehaviorContext context, Status status) {
        currentBehavior = null;
        super.onEnd(context, status);
    }
    
    private BehaviorLearner getEntityLearner(BehaviorContext context) {
        // Get learner from blackboard or create new one
        return context.getBlackboard()
            .get("ml_learner")
            .filter(obj -> obj instanceof BehaviorLearner)
            .map(obj -> (BehaviorLearner) obj)
            .orElseGet(() -> {
                BehaviorLearner newLearner = new BehaviorLearner(context.getEntity());
                context.getBlackboard().set("ml_learner", newLearner);
                return newLearner;
            });
    }
    
    private float calculateReward(BehaviorContext context, boolean success) {
        if (!success) {
            return 0.0f;
        }
        
        // Base reward
        float reward = 0.5f;
        
        // Bonus for quick completion
        long duration = System.currentTimeMillis() - actionStartTime;
        if (duration < 5000) { // Less than 5 seconds
            reward += 0.3f;
        }
        
        // Bonus for entity health
        float healthPercent = context.getEntity().getHealth() / context.getEntity().getMaxHealth();
        reward += healthPercent * 0.2f;
        
        return Math.min(1.0f, reward);
    }
    
    /**
     * Gets the learner for inspection.
     * 
     * @param context Current context
     * @return The behavior learner
     */
    public BehaviorLearner getLearner(BehaviorContext context) {
        return getEntityLearner(context);
    }
    
    /**
     * Gets all behavior options.
     * 
     * @return List of behaviors
     */
    public List<NamedBehavior> getBehaviors() {
        return new ArrayList<>(behaviors);
    }
    
    /**
     * A named behavior option.
     */
    public static class NamedBehavior {
        private final String name;
        private final Behavior behavior;
        
        public NamedBehavior(String name, Behavior behavior) {
            this.name = name;
            this.behavior = behavior;
        }
        
        public String getName() { return name; }
        public Behavior getBehavior() { return behavior; }
    }
}
