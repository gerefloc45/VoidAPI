package com.gerefloc45.voidapi.api.goap;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Represents the state of the world for GOAP planning.
 * 
 * <p>WorldState is a key-value store that tracks various conditions
 * and properties of the game world. Each state is immutable for planning
 * purposes, but can be copied and modified to create new states.
 * 
 * <p><b>Example:</b>
 * <pre>{@code
 * WorldState state = new WorldState();
 * state.set("hasWeapon", true);
 * state.set("enemyNearby", false);
 * state.set("health", 100);
 * }</pre>
 * 
 * @since 0.4.0
 */
public class WorldState {
    private final Map<String, Object> state;
    
    /**
     * Creates a new empty world state.
     */
    public WorldState() {
        this.state = new HashMap<>();
    }
    
    /**
     * Creates a copy of an existing world state.
     * 
     * @param other The state to copy
     */
    public WorldState(WorldState other) {
        this.state = new HashMap<>(other.state);
    }
    
    /**
     * Sets a value in the world state.
     * 
     * @param key The state key
     * @param value The state value
     */
    public void set(String key, Object value) {
        state.put(key, value);
    }
    
    /**
     * Gets a value from the world state.
     * 
     * @param key The state key
     * @return The state value, or null if not found
     */
    public Object get(String key) {
        return state.get(key);
    }
    
    /**
     * Gets a typed value from the world state.
     * 
     * @param <T> The expected type
     * @param key The state key
     * @param type The value class
     * @return The state value, or null if not found or wrong type
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> type) {
        Object value = state.get(key);
        if (value != null && type.isInstance(value)) {
            return (T) value;
        }
        return null;
    }
    
    /**
     * Gets a typed value with a default.
     * 
     * @param <T> The expected type
     * @param key The state key
     * @param type The value class
     * @param defaultValue The default value if not found
     * @return The state value or default
     */
    public <T> T getOrDefault(String key, Class<T> type, T defaultValue) {
        T value = get(key, type);
        return value != null ? value : defaultValue;
    }
    
    /**
     * Checks if a key exists in the state.
     * 
     * @param key The state key
     * @return True if the key exists
     */
    public boolean has(String key) {
        return state.containsKey(key);
    }
    
    /**
     * Removes a value from the state.
     * 
     * @param key The state key
     */
    public void remove(String key) {
        state.remove(key);
    }
    
    /**
     * Creates a copy of this world state.
     * 
     * @return A new WorldState with the same values
     */
    public WorldState copy() {
        return new WorldState(this);
    }
    
    /**
     * Checks if this state satisfies all conditions in another state.
     * 
     * <p>This is used to check if preconditions are met.
     * 
     * @param conditions The conditions to check
     * @return True if all conditions are satisfied
     */
    public boolean satisfies(WorldState conditions) {
        for (Map.Entry<String, Object> entry : conditions.state.entrySet()) {
            Object ourValue = state.get(entry.getKey());
            if (!Objects.equals(ourValue, entry.getValue())) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Applies effects from another state to this state.
     * 
     * @param effects The effects to apply
     */
    public void apply(WorldState effects) {
        state.putAll(effects.state);
    }
    
    /**
     * Gets the number of differences between this state and a goal state.
     * 
     * <p>This is used as a heuristic for A* planning.
     * 
     * @param goal The goal state
     * @return The number of differing values
     */
    public int countDifferences(WorldState goal) {
        int differences = 0;
        for (Map.Entry<String, Object> entry : goal.state.entrySet()) {
            Object ourValue = state.get(entry.getKey());
            if (!Objects.equals(ourValue, entry.getValue())) {
                differences++;
            }
        }
        return differences;
    }
    
    /**
     * Gets all state keys.
     * 
     * @return A set of all keys
     */
    public java.util.Set<String> keys() {
        return state.keySet();
    }
    
    /**
     * Clears all state values.
     */
    public void clear() {
        state.clear();
    }
    
    /**
     * Gets the number of state entries.
     * 
     * @return The state size
     */
    public int size() {
        return state.size();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof WorldState)) return false;
        WorldState other = (WorldState) obj;
        return state.equals(other.state);
    }
    
    @Override
    public int hashCode() {
        return state.hashCode();
    }
    
    @Override
    public String toString() {
        return "WorldState" + state.toString();
    }
}
