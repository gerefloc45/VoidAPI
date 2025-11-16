package com.gerefloc45.voidapi.api.ml;

import com.gerefloc45.voidapi.api.BehaviorContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;

import java.util.*;

/**
 * Learns entity behaviors from player interactions and outcomes.
 * 
 * <p>This system observes entity actions and their results, building
 * a model of which behaviors are most effective in different situations.
 * 
 * <p><b>Features:</b>
 * <ul>
 *   <li>Action-outcome tracking</li>
 *   <li>Success rate calculation</li>
 *   <li>Context-aware learning</li>
 *   <li>Behavior reinforcement</li>
 * </ul>
 * 
 * <p><b>Example:</b>
 * <pre>{@code
 * BehaviorLearner learner = new BehaviorLearner(entity);
 * 
 * // Record action
 * learner.recordAction("attack", context);
 * 
 * // Record outcome
 * learner.recordOutcome("attack", true, 1.0f); // Success
 * 
 * // Get best action for situation
 * String bestAction = learner.getBestAction(context);
 * }</pre>
 * 
 * @since 0.5.0
 */
public class BehaviorLearner {
    private final Map<String, ActionStats> actionStats;
    private final List<LearningEvent> history;
    private final int maxHistorySize;
    
    private float learningRate = 0.1f;
    private float explorationRate = 0.2f;
    private boolean enabled = true;
    
    /**
     * Creates a new behavior learner.
     * 
     * @param entity The entity to learn for
     */
    public BehaviorLearner(LivingEntity entity) {
        this(entity.getUuid(), 1000);
    }
    
    /**
     * Creates a new behavior learner with custom history size.
     * 
     * @param entityId The entity UUID
     * @param maxHistorySize Maximum events to remember
     */
    public BehaviorLearner(UUID entityId, int maxHistorySize) {
        this.actionStats = new HashMap<>();
        this.history = new ArrayList<>();
        this.maxHistorySize = maxHistorySize;
    }
    
    /**
     * Records an action being taken.
     * 
     * @param actionName Name of the action
     * @param context Current context
     */
    public void recordAction(String actionName, BehaviorContext context) {
        if (!enabled) return;
        
        ActionStats stats = actionStats.computeIfAbsent(actionName, k -> new ActionStats(actionName));
        stats.attempts++;
        
        LearningEvent event = new LearningEvent(actionName, context, System.currentTimeMillis());
        history.add(event);
        
        // Trim history if needed
        if (history.size() > maxHistorySize) {
            history.remove(0);
        }
    }
    
    /**
     * Records the outcome of an action.
     * 
     * @param actionName Name of the action
     * @param success Whether it succeeded
     * @param reward Reward value (0-1)
     */
    public void recordOutcome(String actionName, boolean success, float reward) {
        if (!enabled) return;
        
        ActionStats stats = actionStats.get(actionName);
        if (stats == null) return;
        
        if (success) {
            stats.successes++;
        }
        
        // Update Q-value with learning rate
        float oldValue = stats.qValue;
        stats.qValue = oldValue + learningRate * (reward - oldValue);
        stats.lastReward = reward;
        stats.lastUpdate = System.currentTimeMillis();
    }
    
    /**
     * Gets the best action for the current context.
     * 
     * @param context Current context
     * @return Best action name, or null if none learned
     */
    public String getBestAction(BehaviorContext context) {
        if (!enabled || actionStats.isEmpty()) {
            return null;
        }
        
        // Epsilon-greedy: sometimes explore
        if (Math.random() < explorationRate) {
            // Random action (exploration)
            List<String> actions = new ArrayList<>(actionStats.keySet());
            return actions.get((int)(Math.random() * actions.size()));
        }
        
        // Best action (exploitation)
        return actionStats.values().stream()
            .max(Comparator.comparingDouble(ActionStats::getScore))
            .map(stats -> stats.actionName)
            .orElse(null);
    }
    
    /**
     * Gets success rate for an action.
     * 
     * @param actionName Action name
     * @return Success rate (0-1), or 0 if unknown
     */
    public float getSuccessRate(String actionName) {
        ActionStats stats = actionStats.get(actionName);
        if (stats == null || stats.attempts == 0) {
            return 0.0f;
        }
        return (float) stats.successes / stats.attempts;
    }
    
    /**
     * Gets Q-value for an action.
     * 
     * @param actionName Action name
     * @return Q-value, or 0 if unknown
     */
    public float getQValue(String actionName) {
        ActionStats stats = actionStats.get(actionName);
        return stats != null ? stats.qValue : 0.0f;
    }
    
    /**
     * Resets all learned data.
     */
    public void reset() {
        actionStats.clear();
        history.clear();
    }
    
    /**
     * Saves learner state to NBT.
     * 
     * @param nbt NBT compound to save to
     */
    public void writeToNbt(NbtCompound nbt) {
        nbt.putFloat("learningRate", learningRate);
        nbt.putFloat("explorationRate", explorationRate);
        nbt.putBoolean("enabled", enabled);
        
        NbtCompound statsNbt = new NbtCompound();
        for (Map.Entry<String, ActionStats> entry : actionStats.entrySet()) {
            NbtCompound statNbt = new NbtCompound();
            ActionStats stats = entry.getValue();
            statNbt.putInt("attempts", stats.attempts);
            statNbt.putInt("successes", stats.successes);
            statNbt.putFloat("qValue", stats.qValue);
            statNbt.putFloat("lastReward", stats.lastReward);
            statNbt.putLong("lastUpdate", stats.lastUpdate);
            statsNbt.put(entry.getKey(), statNbt);
        }
        nbt.put("stats", statsNbt);
    }
    
    /**
     * Loads learner state from NBT.
     * 
     * @param nbt NBT compound to load from
     */
    public void readFromNbt(NbtCompound nbt) {
        learningRate = nbt.getFloat("learningRate");
        explorationRate = nbt.getFloat("explorationRate");
        enabled = nbt.getBoolean("enabled");
        
        actionStats.clear();
        NbtCompound statsNbt = nbt.getCompound("stats");
        for (String key : statsNbt.getKeys()) {
            NbtCompound statNbt = statsNbt.getCompound(key);
            ActionStats stats = new ActionStats(key);
            stats.attempts = statNbt.getInt("attempts");
            stats.successes = statNbt.getInt("successes");
            stats.qValue = statNbt.getFloat("qValue");
            stats.lastReward = statNbt.getFloat("lastReward");
            stats.lastUpdate = statNbt.getLong("lastUpdate");
            actionStats.put(key, stats);
        }
    }
    
    // Getters and setters
    
    public float getLearningRate() {
        return learningRate;
    }
    
    public void setLearningRate(float learningRate) {
        this.learningRate = Math.max(0.0f, Math.min(1.0f, learningRate));
    }
    
    public float getExplorationRate() {
        return explorationRate;
    }
    
    public void setExplorationRate(float explorationRate) {
        this.explorationRate = Math.max(0.0f, Math.min(1.0f, explorationRate));
    }
    
    public boolean isEnabled() {
        return enabled;
    }
    
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    public Map<String, ActionStats> getActionStats() {
        return Collections.unmodifiableMap(actionStats);
    }
    
    public List<LearningEvent> getHistory() {
        return Collections.unmodifiableList(history);
    }
    
    /**
     * Statistics for a learned action.
     */
    public static class ActionStats {
        private final String actionName;
        private int attempts;
        private int successes;
        private float qValue;
        private float lastReward;
        private long lastUpdate;
        
        public ActionStats(String actionName) {
            this.actionName = actionName;
            this.attempts = 0;
            this.successes = 0;
            this.qValue = 0.0f;
            this.lastReward = 0.0f;
            this.lastUpdate = System.currentTimeMillis();
        }
        
        public double getScore() {
            // Combine Q-value with success rate
            float successRate = attempts > 0 ? (float) successes / attempts : 0.0f;
            return qValue * 0.7 + successRate * 0.3;
        }
        
        public String getActionName() { return actionName; }
        public int getAttempts() { return attempts; }
        public int getSuccesses() { return successes; }
        public float getQValue() { return qValue; }
        public float getLastReward() { return lastReward; }
        public long getLastUpdate() { return lastUpdate; }
    }
    
    /**
     * A recorded learning event.
     */
    public static class LearningEvent {
        private final String actionName;
        private final BehaviorContext context;
        private final long timestamp;
        
        public LearningEvent(String actionName, BehaviorContext context, long timestamp) {
            this.actionName = actionName;
            this.context = context;
            this.timestamp = timestamp;
        }
        
        public String getActionName() { return actionName; }
        public BehaviorContext getContext() { return context; }
        public long getTimestamp() { return timestamp; }
    }
}
