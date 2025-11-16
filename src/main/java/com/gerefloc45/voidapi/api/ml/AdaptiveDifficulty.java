package com.gerefloc45.voidapi.api.ml;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

import java.util.*;

/**
 * Dynamically adjusts AI difficulty based on player performance.
 * 
 * <p>This system monitors player success/failure rates and adjusts
 * entity behavior to maintain an appropriate challenge level.
 * 
 * <p><b>Adjustment Factors:</b>
 * <ul>
 *   <li>Player win/loss ratio</li>
 *   <li>Average combat duration</li>
 *   <li>Player health trends</li>
 *   <li>Escape success rate</li>
 * </ul>
 * 
 * <p><b>Difficulty Modifiers:</b>
 * <ul>
 *   <li>Reaction time</li>
 *   <li>Accuracy</li>
 *   <li>Aggression level</li>
 *   <li>Decision making speed</li>
 * </ul>
 * 
 * <p><b>Example:</b>
 * <pre>{@code
 * AdaptiveDifficulty difficulty = new AdaptiveDifficulty(entity);
 * 
 * // Record combat outcome
 * difficulty.recordCombatEnd(player, true); // Player won
 * 
 * // Get current difficulty level
 * float level = difficulty.getDifficultyLevel(); // 0-1
 * 
 * // Apply difficulty modifiers
 * float reactionTime = difficulty.getReactionTimeMultiplier();
 * float accuracy = difficulty.getAccuracyMultiplier();
 * }</pre>
 * 
 * @since 0.5.0
 */
public class AdaptiveDifficulty {
    private final Map<UUID, PlayerDifficultyProfile> playerProfiles;
    
    private float baseDifficulty = 0.5f;
    private float adaptationRate = 0.05f;
    private float minDifficulty = 0.1f;
    private float maxDifficulty = 1.0f;
    private boolean enabled = true;
    
    /**
     * Creates a new adaptive difficulty system.
     * 
     * @param entity The entity to adapt
     */
    public AdaptiveDifficulty(LivingEntity entity) {
        this.playerProfiles = new HashMap<>();
    }
    
    /**
     * Records the start of combat.
     * 
     * @param player The player
     */
    public void recordCombatStart(PlayerEntity player) {
        if (!enabled) return;
        
        PlayerDifficultyProfile profile = getOrCreateProfile(player);
        profile.combatStartTime = System.currentTimeMillis();
        profile.combatCount++;
    }
    
    /**
     * Records the end of combat.
     * 
     * @param player The player
     * @param playerWon Whether the player won
     */
    public void recordCombatEnd(PlayerEntity player, boolean playerWon) {
        if (!enabled) return;
        
        PlayerDifficultyProfile profile = getOrCreateProfile(player);
        
        long duration = System.currentTimeMillis() - profile.combatStartTime;
        profile.totalCombatTime += duration;
        
        if (playerWon) {
            profile.playerWins++;
            // Player winning too much - increase difficulty
            adjustDifficulty(profile, adaptationRate);
        } else {
            profile.entityWins++;
            // Player losing too much - decrease difficulty
            adjustDifficulty(profile, -adaptationRate);
        }
    }
    
    /**
     * Records player health after combat.
     * 
     * @param player The player
     * @param healthPercent Health percentage (0-1)
     */
    public void recordPlayerHealth(PlayerEntity player, float healthPercent) {
        if (!enabled) return;
        
        PlayerDifficultyProfile profile = getOrCreateProfile(player);
        profile.healthHistory.add(healthPercent);
        
        // Keep only recent history
        if (profile.healthHistory.size() > 20) {
            profile.healthHistory.remove(0);
        }
        
        // If player consistently low health, decrease difficulty
        float avgHealth = (float) profile.healthHistory.stream()
            .mapToDouble(Float::doubleValue)
            .average()
            .orElse(1.0);
        
        if (avgHealth < 0.3f) {
            adjustDifficulty(profile, -adaptationRate * 0.5f);
        }
    }
    
    /**
     * Gets difficulty level for a player (0-1).
     * 
     * @param player The player
     * @return Difficulty level
     */
    public float getDifficultyLevel(PlayerEntity player) {
        PlayerDifficultyProfile profile = playerProfiles.get(player.getUuid());
        return profile != null ? profile.difficulty : baseDifficulty;
    }
    
    /**
     * Gets reaction time multiplier (0.5-2.0).
     * Lower = faster reactions.
     * 
     * @param player The player
     * @return Multiplier
     */
    public float getReactionTimeMultiplier(PlayerEntity player) {
        float difficulty = getDifficultyLevel(player);
        // Easy: 2.0 (slow), Hard: 0.5 (fast)
        return 2.0f - (difficulty * 1.5f);
    }
    
    /**
     * Gets accuracy multiplier (0.5-1.5).
     * Higher = more accurate.
     * 
     * @param player The player
     * @return Multiplier
     */
    public float getAccuracyMultiplier(PlayerEntity player) {
        float difficulty = getDifficultyLevel(player);
        // Easy: 0.5, Hard: 1.5
        return 0.5f + difficulty;
    }
    
    /**
     * Gets aggression multiplier (0.5-1.5).
     * Higher = more aggressive.
     * 
     * @param player The player
     * @return Multiplier
     */
    public float getAggressionMultiplier(PlayerEntity player) {
        float difficulty = getDifficultyLevel(player);
        // Easy: 0.5, Hard: 1.5
        return 0.5f + difficulty;
    }
    
    /**
     * Gets decision speed multiplier (0.5-2.0).
     * Higher = faster decisions.
     * 
     * @param player The player
     * @return Multiplier
     */
    public float getDecisionSpeedMultiplier(PlayerEntity player) {
        float difficulty = getDifficultyLevel(player);
        // Easy: 0.5 (slow), Hard: 2.0 (fast)
        return 0.5f + (difficulty * 1.5f);
    }
    
    /**
     * Gets player win rate.
     * 
     * @param player The player
     * @return Win rate (0-1)
     */
    public float getPlayerWinRate(PlayerEntity player) {
        PlayerDifficultyProfile profile = playerProfiles.get(player.getUuid());
        if (profile == null || profile.combatCount == 0) {
            return 0.5f;
        }
        return (float) profile.playerWins / profile.combatCount;
    }
    
    /**
     * Gets average combat duration in seconds.
     * 
     * @param player The player
     * @return Average duration
     */
    public float getAverageCombatDuration(PlayerEntity player) {
        PlayerDifficultyProfile profile = playerProfiles.get(player.getUuid());
        if (profile == null || profile.combatCount == 0) {
            return 0.0f;
        }
        return (float) profile.totalCombatTime / profile.combatCount / 1000.0f;
    }
    
    private void adjustDifficulty(PlayerDifficultyProfile profile, float amount) {
        profile.difficulty += amount;
        profile.difficulty = Math.max(minDifficulty, Math.min(maxDifficulty, profile.difficulty));
    }
    
    private PlayerDifficultyProfile getOrCreateProfile(PlayerEntity player) {
        return playerProfiles.computeIfAbsent(
            player.getUuid(),
            id -> new PlayerDifficultyProfile(id, baseDifficulty)
        );
    }
    
    /**
     * Resets all difficulty data.
     */
    public void reset() {
        playerProfiles.clear();
    }
    
    /**
     * Saves difficulty state to NBT.
     * 
     * @param nbt NBT compound to save to
     */
    public void writeToNbt(NbtCompound nbt) {
        nbt.putFloat("baseDifficulty", baseDifficulty);
        nbt.putFloat("adaptationRate", adaptationRate);
        nbt.putBoolean("enabled", enabled);
        
        NbtCompound profilesNbt = new NbtCompound();
        for (Map.Entry<UUID, PlayerDifficultyProfile> entry : playerProfiles.entrySet()) {
            NbtCompound profileNbt = new NbtCompound();
            PlayerDifficultyProfile profile = entry.getValue();
            
            profileNbt.putFloat("difficulty", profile.difficulty);
            profileNbt.putInt("combatCount", profile.combatCount);
            profileNbt.putInt("playerWins", profile.playerWins);
            profileNbt.putInt("entityWins", profile.entityWins);
            profileNbt.putLong("totalCombatTime", profile.totalCombatTime);
            
            profilesNbt.put(entry.getKey().toString(), profileNbt);
        }
        nbt.put("profiles", profilesNbt);
    }
    
    /**
     * Loads difficulty state from NBT.
     * 
     * @param nbt NBT compound to load from
     */
    public void readFromNbt(NbtCompound nbt) {
        baseDifficulty = nbt.getFloat("baseDifficulty");
        adaptationRate = nbt.getFloat("adaptationRate");
        enabled = nbt.getBoolean("enabled");
        
        playerProfiles.clear();
        NbtCompound profilesNbt = nbt.getCompound("profiles");
        for (String key : profilesNbt.getKeys()) {
            try {
                UUID playerId = UUID.fromString(key);
                NbtCompound profileNbt = profilesNbt.getCompound(key);
                
                PlayerDifficultyProfile profile = new PlayerDifficultyProfile(playerId, baseDifficulty);
                profile.difficulty = profileNbt.getFloat("difficulty");
                profile.combatCount = profileNbt.getInt("combatCount");
                profile.playerWins = profileNbt.getInt("playerWins");
                profile.entityWins = profileNbt.getInt("entityWins");
                profile.totalCombatTime = profileNbt.getLong("totalCombatTime");
                
                playerProfiles.put(playerId, profile);
            } catch (IllegalArgumentException e) {
                // Invalid UUID, skip
            }
        }
    }
    
    // Getters and setters
    
    public float getBaseDifficulty() {
        return baseDifficulty;
    }
    
    public void setBaseDifficulty(float baseDifficulty) {
        this.baseDifficulty = Math.max(minDifficulty, Math.min(maxDifficulty, baseDifficulty));
    }
    
    public float getAdaptationRate() {
        return adaptationRate;
    }
    
    public void setAdaptationRate(float adaptationRate) {
        this.adaptationRate = Math.max(0.0f, Math.min(1.0f, adaptationRate));
    }
    
    public boolean isEnabled() {
        return enabled;
    }
    
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    /**
     * Player difficulty profile.
     */
    private static class PlayerDifficultyProfile {
        private float difficulty;
        private int combatCount;
        private int playerWins;
        private int entityWins;
        private long totalCombatTime;
        private long combatStartTime;
        private final List<Float> healthHistory;
        
        public PlayerDifficultyProfile(UUID playerId, float initialDifficulty) {
            this.difficulty = initialDifficulty;
            this.combatCount = 0;
            this.playerWins = 0;
            this.entityWins = 0;
            this.totalCombatTime = 0;
            this.combatStartTime = 0;
            this.healthHistory = new ArrayList<>();
        }
    }
}
