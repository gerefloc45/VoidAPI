package com.gerefloc45.voidapi.api.ml;

import com.gerefloc45.voidapi.api.BehaviorContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

import java.util.*;

/**
 * Recognizes patterns in player behavior.
 * 
 * <p>This system tracks player actions over time and identifies
 * recurring patterns that can be predicted and responded to.
 * 
 * <p><b>Detected Patterns:</b>
 * <ul>
 *   <li>Movement patterns (patrol routes, escape paths)</li>
 *   <li>Combat patterns (attack timing, retreat conditions)</li>
 *   <li>Resource gathering patterns</li>
 *   <li>Time-based patterns (day/night behavior)</li>
 * </ul>
 * 
 * <p><b>Example:</b>
 * <pre>{@code
 * PatternRecognizer recognizer = new PatternRecognizer();
 * 
 * // Record player actions
 * recognizer.recordPlayerAction(player, "attack", context);
 * recognizer.recordPlayerMovement(player, position);
 * 
 * // Detect patterns
 * List<Pattern> patterns = recognizer.detectPatterns(player);
 * 
 * // Predict next action
 * String prediction = recognizer.predictNextAction(player);
 * }</pre>
 * 
 * @since 0.5.0
 */
public class PatternRecognizer {
    private final Map<UUID, PlayerProfile> playerProfiles;
    private final int maxHistorySize;
    private final int minPatternLength;
    private final float similarityThreshold;
    
    /**
     * Creates a new pattern recognizer.
     */
    public PatternRecognizer() {
        this(500, 3, 0.7f);
    }
    
    /**
     * Creates a new pattern recognizer with custom parameters.
     * 
     * @param maxHistorySize Maximum actions to remember per player
     * @param minPatternLength Minimum pattern length to detect
     * @param similarityThreshold Threshold for pattern matching (0-1)
     */
    public PatternRecognizer(int maxHistorySize, int minPatternLength, float similarityThreshold) {
        this.playerProfiles = new HashMap<>();
        this.maxHistorySize = maxHistorySize;
        this.minPatternLength = minPatternLength;
        this.similarityThreshold = similarityThreshold;
    }
    
    /**
     * Records a player action.
     * 
     * @param player The player
     * @param actionType Type of action
     * @param context Current context
     */
    public void recordPlayerAction(PlayerEntity player, String actionType, BehaviorContext context) {
        PlayerProfile profile = getOrCreateProfile(player);
        
        PlayerAction action = new PlayerAction(
            actionType,
            player.getPos(),
            context.getEntity().getPos(),
            System.currentTimeMillis()
        );
        
        profile.addAction(action);
    }
    
    /**
     * Records player movement.
     * 
     * @param player The player
     * @param position Current position
     */
    public void recordPlayerMovement(PlayerEntity player, Vec3d position) {
        PlayerProfile profile = getOrCreateProfile(player);
        profile.addMovement(position, System.currentTimeMillis());
    }
    
    /**
     * Detects patterns in player behavior.
     * 
     * @param player The player
     * @return List of detected patterns
     */
    public List<Pattern> detectPatterns(PlayerEntity player) {
        PlayerProfile profile = playerProfiles.get(player.getUuid());
        if (profile == null) {
            return Collections.emptyList();
        }
        
        List<Pattern> patterns = new ArrayList<>();
        
        // Detect action sequences
        patterns.addAll(detectActionSequences(profile));
        
        // Detect movement patterns
        patterns.addAll(detectMovementPatterns(profile));
        
        // Detect time-based patterns
        patterns.addAll(detectTimePatterns(profile));
        
        return patterns;
    }
    
    /**
     * Predicts the next likely action.
     * 
     * @param player The player
     * @return Predicted action type, or null if unknown
     */
    public String predictNextAction(PlayerEntity player) {
        PlayerProfile profile = playerProfiles.get(player.getUuid());
        if (profile == null || profile.actions.size() < minPatternLength) {
            return null;
        }
        
        // Get recent action sequence
        List<PlayerAction> recent = profile.getRecentActions(minPatternLength);
        
        // Find matching historical sequences
        Map<String, Integer> predictions = new HashMap<>();
        
        for (int i = 0; i < profile.actions.size() - minPatternLength; i++) {
            List<PlayerAction> sequence = profile.actions.subList(i, i + minPatternLength);
            
            if (sequencesMatch(recent, sequence)) {
                // Found matching sequence, record what came next
                if (i + minPatternLength < profile.actions.size()) {
                    String nextAction = profile.actions.get(i + minPatternLength).actionType;
                    predictions.merge(nextAction, 1, Integer::sum);
                }
            }
        }
        
        // Return most common prediction
        return predictions.entrySet().stream()
            .max(Comparator.comparingInt(Map.Entry::getValue))
            .map(Map.Entry::getKey)
            .orElse(null);
    }
    
    /**
     * Gets player's aggression level (0-1).
     * 
     * @param player The player
     * @return Aggression level
     */
    public float getAggressionLevel(PlayerEntity player) {
        PlayerProfile profile = playerProfiles.get(player.getUuid());
        if (profile == null || profile.actions.isEmpty()) {
            return 0.5f;
        }
        
        long recentActions = profile.actions.stream()
            .filter(a -> a.actionType.equals("attack"))
            .count();
        
        return Math.min(1.0f, (float) recentActions / profile.actions.size());
    }
    
    /**
     * Gets player's retreat tendency (0-1).
     * 
     * @param player The player
     * @return Retreat tendency
     */
    public float getRetreatTendency(PlayerEntity player) {
        PlayerProfile profile = playerProfiles.get(player.getUuid());
        if (profile == null || profile.movements.isEmpty()) {
            return 0.5f;
        }
        
        // Analyze movement patterns for retreating behavior
        int retreatCount = 0;
        for (int i = 1; i < profile.movements.size(); i++) {
            Vec3d prev = profile.movements.get(i - 1).position;
            Vec3d curr = profile.movements.get(i).position;
            
            // If moving away from entity, count as retreat
            // (Simplified - would need entity position tracking)
            double distance = curr.distanceTo(prev);
            if (distance > 2.0) {
                retreatCount++;
            }
        }
        
        return Math.min(1.0f, (float) retreatCount / profile.movements.size());
    }
    
    private PlayerProfile getOrCreateProfile(PlayerEntity player) {
        return playerProfiles.computeIfAbsent(
            player.getUuid(),
            id -> new PlayerProfile(id, maxHistorySize)
        );
    }
    
    private List<Pattern> detectActionSequences(PlayerProfile profile) {
        List<Pattern> patterns = new ArrayList<>();
        
        // Simple sequence detection
        for (int length = minPatternLength; length <= 5; length++) {
            Map<String, Integer> sequences = new HashMap<>();
            
            for (int i = 0; i <= profile.actions.size() - length; i++) {
                StringBuilder sequence = new StringBuilder();
                for (int j = 0; j < length; j++) {
                    sequence.append(profile.actions.get(i + j).actionType).append("-");
                }
                String key = sequence.toString();
                sequences.merge(key, 1, Integer::sum);
            }
            
            // Add frequent sequences as patterns
            sequences.entrySet().stream()
                .filter(e -> e.getValue() >= 3) // Occurred at least 3 times
                .forEach(e -> patterns.add(new Pattern(
                    "action_sequence",
                    e.getKey(),
                    (float) e.getValue() / profile.actions.size()
                )));
        }
        
        return patterns;
    }
    
    private List<Pattern> detectMovementPatterns(PlayerProfile profile) {
        // Simplified - would implement clustering for patrol routes
        return Collections.emptyList();
    }
    
    private List<Pattern> detectTimePatterns(PlayerProfile profile) {
        // Simplified - would analyze time-of-day correlations
        return Collections.emptyList();
    }
    
    private boolean sequencesMatch(List<PlayerAction> seq1, List<PlayerAction> seq2) {
        if (seq1.size() != seq2.size()) {
            return false;
        }
        
        int matches = 0;
        for (int i = 0; i < seq1.size(); i++) {
            if (seq1.get(i).actionType.equals(seq2.get(i).actionType)) {
                matches++;
            }
        }
        
        return (float) matches / seq1.size() >= similarityThreshold;
    }
    
    /**
     * Clears all learned patterns.
     */
    public void reset() {
        playerProfiles.clear();
    }
    
    /**
     * Player behavior profile.
     */
    private static class PlayerProfile {
        private final List<PlayerAction> actions;
        private final List<MovementRecord> movements;
        private final int maxSize;
        
        public PlayerProfile(UUID playerId, int maxSize) {
            this.actions = new ArrayList<>();
            this.movements = new ArrayList<>();
            this.maxSize = maxSize;
        }
        
        public void addAction(PlayerAction action) {
            actions.add(action);
            if (actions.size() > maxSize) {
                actions.remove(0);
            }
        }
        
        public void addMovement(Vec3d position, long timestamp) {
            movements.add(new MovementRecord(position, timestamp));
            if (movements.size() > maxSize) {
                movements.remove(0);
            }
        }
        
        public List<PlayerAction> getRecentActions(int count) {
            int start = Math.max(0, actions.size() - count);
            return new ArrayList<>(actions.subList(start, actions.size()));
        }
    }
    
    /**
     * A recorded player action.
     */
    private static class PlayerAction {
        private final String actionType;
        
        public PlayerAction(String actionType, Vec3d playerPosition, Vec3d entityPosition, long timestamp) {
            this.actionType = actionType;
        }
    }
    
    /**
     * A movement record.
     */
    private static class MovementRecord {
        private final Vec3d position;
        
        public MovementRecord(Vec3d position, long timestamp) {
            this.position = position;
        }
    }
    
    /**
     * A detected pattern.
     */
    public static class Pattern {
        private final String type;
        private final String description;
        private final float confidence;
        
        public Pattern(String type, String description, float confidence) {
            this.type = type;
            this.description = description;
            this.confidence = confidence;
        }
        
        public String getType() { return type; }
        public String getDescription() { return description; }
        public float getConfidence() { return confidence; }
    }
}
