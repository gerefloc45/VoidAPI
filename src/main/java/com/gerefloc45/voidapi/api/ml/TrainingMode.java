package com.gerefloc45.voidapi.api.ml;

import com.gerefloc45.voidapi.api.BehaviorContext;
import com.gerefloc45.voidapi.api.Behavior;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;

import java.util.*;

/**
 * Training mode for supervised learning of entity behaviors.
 * 
 * <p>This system allows entities to learn from demonstrations,
 * recording expert behavior and replicating it in similar situations.
 * 
 * <p><b>Training Process:</b>
 * <ol>
 *   <li>Enable training mode</li>
 *   <li>Demonstrate desired behavior</li>
 *   <li>System records state-action pairs</li>
 *   <li>Entity learns to replicate behavior</li>
 * </ol>
 * 
 * <p><b>Example:</b>
 * <pre>{@code
 * TrainingMode training = new TrainingMode(entity);
 * 
 * // Start training
 * training.startTraining("patrol_route");
 * 
 * // Record demonstrations
 * training.recordDemonstration(context, selectedBehavior);
 * 
 * // End training
 * training.endTraining();
 * 
 * // Use learned behavior
 * Behavior learned = training.getLearnedBehavior(context);
 * }</pre>
 * 
 * @since 0.5.0
 */
public class TrainingMode {
    private final Map<String, TrainingSession> sessions;
    private TrainingSession currentSession;
    
    private boolean enabled = false;
    private int maxDemonstrations = 100;
    
    /**
     * Creates a new training mode system.
     * 
     * @param entity The entity to train
     */
    public TrainingMode(LivingEntity entity) {
        this.sessions = new HashMap<>();
        this.currentSession = null;
    }
    
    /**
     * Starts a new training session.
     * 
     * @param sessionName Name for this training session
     */
    public void startTraining(String sessionName) {
        currentSession = new TrainingSession(sessionName);
        enabled = true;
    }
    
    /**
     * Ends the current training session.
     */
    public void endTraining() {
        if (currentSession != null) {
            sessions.put(currentSession.name, currentSession);
            currentSession = null;
        }
        enabled = false;
    }
    
    /**
     * Records a demonstration.
     * 
     * @param context Current context
     * @param behavior Behavior being demonstrated
     */
    public void recordDemonstration(BehaviorContext context, Behavior behavior) {
        if (!enabled || currentSession == null) {
            return;
        }
        
        // Extract relevant state features
        StateFeatures features = extractFeatures(context);
        
        // Record state-action pair
        Demonstration demo = new Demonstration(features, behavior, System.currentTimeMillis());
        currentSession.addDemonstration(demo);
    }
    
    /**
     * Gets learned behavior for current context.
     * 
     * @param context Current context
     * @return Best matching learned behavior, or null
     */
    public Behavior getLearnedBehavior(BehaviorContext context) {
        if (sessions.isEmpty()) {
            return null;
        }
        
        StateFeatures currentFeatures = extractFeatures(context);
        
        // Find most similar demonstration
        Demonstration bestMatch = null;
        float bestSimilarity = 0.0f;
        
        for (TrainingSession session : sessions.values()) {
            for (Demonstration demo : session.demonstrations) {
                float similarity = currentFeatures.similarityTo(demo.features);
                if (similarity > bestSimilarity) {
                    bestSimilarity = similarity;
                    bestMatch = demo;
                }
            }
        }
        
        // Return behavior if similarity is high enough
        return (bestMatch != null && bestSimilarity > 0.7f) ? bestMatch.behavior : null;
    }
    
    /**
     * Gets a specific training session.
     * 
     * @param sessionName Session name
     * @return Training session, or null if not found
     */
    public TrainingSession getSession(String sessionName) {
        return sessions.get(sessionName);
    }
    
    /**
     * Gets all training sessions.
     * 
     * @return Unmodifiable map of sessions
     */
    public Map<String, TrainingSession> getSessions() {
        return Collections.unmodifiableMap(sessions);
    }
    
    /**
     * Deletes a training session.
     * 
     * @param sessionName Session name
     */
    public void deleteSession(String sessionName) {
        sessions.remove(sessionName);
    }
    
    /**
     * Clears all training data.
     */
    public void reset() {
        sessions.clear();
        currentSession = null;
        enabled = false;
    }
    
    private StateFeatures extractFeatures(BehaviorContext context) {
        StateFeatures features = new StateFeatures();
        
        // Extract relevant features from context
        features.entityHealth = context.getEntity().getHealth() / context.getEntity().getMaxHealth();
        features.hasTarget = context.getBlackboard().get("target").isPresent();
        
        // Add more features as needed
        features.timeOfDay = (float) (context.getEntity().getWorld().getTimeOfDay() % 24000) / 24000.0f;
        
        return features;
    }
    
    /**
     * Saves training data to NBT.
     * 
     * @param nbt NBT compound to save to
     */
    public void writeToNbt(NbtCompound nbt) {
        nbt.putBoolean("enabled", enabled);
        nbt.putInt("maxDemonstrations", maxDemonstrations);
        
        NbtCompound sessionsNbt = new NbtCompound();
        for (Map.Entry<String, TrainingSession> entry : sessions.entrySet()) {
            NbtCompound sessionNbt = new NbtCompound();
            TrainingSession session = entry.getValue();
            
            sessionNbt.putString("name", session.name);
            sessionNbt.putLong("startTime", session.startTime);
            sessionNbt.putInt("demonstrationCount", session.demonstrations.size());
            
            sessionsNbt.put(entry.getKey(), sessionNbt);
        }
        nbt.put("sessions", sessionsNbt);
    }
    
    /**
     * Loads training data from NBT.
     * 
     * @param nbt NBT compound to load from
     */
    public void readFromNbt(NbtCompound nbt) {
        enabled = nbt.getBoolean("enabled");
        maxDemonstrations = nbt.getInt("maxDemonstrations");
        
        // Note: Full demonstration data not saved to NBT for performance
        // Only session metadata is persisted
    }
    
    // Getters and setters
    
    public boolean isEnabled() {
        return enabled;
    }
    
    public boolean isTraining() {
        return enabled && currentSession != null;
    }
    
    public int getMaxDemonstrations() {
        return maxDemonstrations;
    }
    
    public void setMaxDemonstrations(int maxDemonstrations) {
        this.maxDemonstrations = Math.max(1, maxDemonstrations);
    }
    
    /**
     * A training session containing demonstrations.
     */
    public static class TrainingSession {
        private final String name;
        private final List<Demonstration> demonstrations;
        private final long startTime;
        private long endTime;
        
        public TrainingSession(String name) {
            this.name = name;
            this.demonstrations = new ArrayList<>();
            this.startTime = System.currentTimeMillis();
            this.endTime = 0;
        }
        
        public void addDemonstration(Demonstration demo) {
            demonstrations.add(demo);
        }
        
        public void end() {
            this.endTime = System.currentTimeMillis();
        }
        
        public String getName() { return name; }
        public List<Demonstration> getDemonstrations() { return Collections.unmodifiableList(demonstrations); }
        public long getStartTime() { return startTime; }
        public long getEndTime() { return endTime; }
        public long getDuration() { return endTime > 0 ? endTime - startTime : System.currentTimeMillis() - startTime; }
    }
    
    /**
     * A single demonstration (state-action pair).
     */
    public static class Demonstration {
        private final StateFeatures features;
        private final Behavior behavior;
        private final long timestamp;
        
        public Demonstration(StateFeatures features, Behavior behavior, long timestamp) {
            this.features = features;
            this.behavior = behavior;
            this.timestamp = timestamp;
        }
        
        public StateFeatures getFeatures() { return features; }
        public Behavior getBehavior() { return behavior; }
        public long getTimestamp() { return timestamp; }
    }
    
    /**
     * State features for matching demonstrations.
     */
    public static class StateFeatures {
        private float entityHealth;
        private boolean hasTarget;
        private float timeOfDay;
        private final Map<String, Float> customFeatures;
        
        public StateFeatures() {
            this.customFeatures = new HashMap<>();
        }
        
        /**
         * Calculates similarity to another state (0-1).
         * 
         * @param other Other state features
         * @return Similarity score
         */
        public float similarityTo(StateFeatures other) {
            float similarity = 0.0f;
            int featureCount = 0;
            
            // Compare health
            similarity += 1.0f - Math.abs(this.entityHealth - other.entityHealth);
            featureCount++;
            
            // Compare target presence
            similarity += (this.hasTarget == other.hasTarget) ? 1.0f : 0.0f;
            featureCount++;
            
            // Compare time of day
            float timeDiff = Math.abs(this.timeOfDay - other.timeOfDay);
            similarity += 1.0f - Math.min(timeDiff, 1.0f - timeDiff) * 2.0f;
            featureCount++;
            
            return featureCount > 0 ? similarity / featureCount : 0.0f;
        }
        
        public void setCustomFeature(String name, float value) {
            customFeatures.put(name, value);
        }
        
        public float getCustomFeature(String name) {
            return customFeatures.getOrDefault(name, 0.0f);
        }
    }
}
