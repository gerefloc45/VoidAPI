# Machine Learning Guide

VoidAPI's Machine Learning system enables AI entities to learn from experience, adapt to player behavior, and dynamically adjust difficulty.

## 📚 Table of Contents

- [What is ML in VoidAPI?](#what-is-ml-in-voidapi)
- [Core Components](#core-components)
- [Behavior Learning](#behavior-learning)
- [Pattern Recognition](#pattern-recognition)
- [Adaptive Difficulty](#adaptive-difficulty)
- [Training Mode](#training-mode)
- [Behavior Tree Integration](#behavior-tree-integration)
- [Data Persistence](#data-persistence)
- [Best Practices](#best-practices)
- [Examples](#examples)

---

## What is ML in VoidAPI?

VoidAPI's ML system provides **lightweight, real-time learning** for game AI:

- 🧠 **Behavior Learning** - Q-learning for action selection
- 🔍 **Pattern Recognition** - Detect and predict player patterns
- ⚖️ **Adaptive Difficulty** - Dynamic challenge adjustment
- 📖 **Training Mode** - Learn from demonstrations
- 🌲 **BT Integration** - Seamless behavior tree integration

### Why Use ML?

✅ **Adaptive** - AI learns and improves over time  
✅ **Personalized** - Adapts to individual players  
✅ **Emergent** - Discovers strategies you didn't program  
✅ **Engaging** - Creates dynamic, unpredictable experiences  

### When to Use ML

- Long-term player interactions
- Personalized difficulty
- Emergent behavior discovery
- Player pattern exploitation
- Dynamic strategy adaptation

---

## Core Components

### 1. BehaviorLearner

Q-learning based system for action selection.

```java
BehaviorLearner learner = new BehaviorLearner(entity);

// Record action taken
learner.recordAction("attack", context);

// Record outcome (success/failure + reward)
learner.recordOutcome("attack", true, 1.0f);

// Get best action for current state
String bestAction = learner.getBestAction(context);
```

### 2. PatternRecognizer

Detects and predicts player behavior patterns.

```java
PatternRecognizer recognizer = new PatternRecognizer();

// Record player actions
recognizer.recordPlayerAction(player, "attack", context);
recognizer.recordPlayerAction(player, "dodge", context);

// Detect patterns
List<Pattern> patterns = recognizer.detectPatterns(player);

// Predict next action
String prediction = recognizer.predictNextAction(player);
```

### 3. AdaptiveDifficulty

Dynamically adjusts AI difficulty per player.

```java
AdaptiveDifficulty difficulty = new AdaptiveDifficulty(entity);

// Record combat results
difficulty.recordCombatEnd(player, playerWon);

// Get difficulty modifiers
float reactionTime = difficulty.getReactionTimeMultiplier(player);
float accuracy = difficulty.getAccuracyMultiplier(player);
float aggression = difficulty.getAggressionMultiplier(player);
```

### 4. TrainingMode

Supervised learning from demonstrations.

```java
TrainingMode training = new TrainingMode(entity);

// Start training session
training.startTraining("patrol_route");

// Record demonstrations
training.recordDemonstration(context, behavior);

// End training
training.endTraining();

// Use learned behavior
Behavior learned = training.getLearnedBehavior(context);
```

### 5. LearningNode

Behavior tree integration for ML.

```java
LearningNode learner = new LearningNode()
    .addBehavior("attack", attackBehavior)
    .addBehavior("flee", fleeBehavior)
    .addBehavior("hide", hideBehavior);

tree.addChild(learner);
```

---

## Behavior Learning

### Basic Q-Learning

```java
import com.gerefloc45.voidapi.api.ml.BehaviorLearner;

public class LearningCombatAI {
    private BehaviorLearner learner;
    
    public void setup(LivingEntity entity) {
        learner = new BehaviorLearner(entity);
        
        // Configure learning parameters
        learner.setLearningRate(0.1f);      // How fast to learn
        learner.setDiscountFactor(0.9f);    // Future reward importance
        learner.setExplorationRate(0.2f);   // Exploration vs exploitation
    }
    
    public void tick(BehaviorContext context) {
        // Get current state
        String state = getStateKey(context);
        
        // Choose action (exploration vs exploitation)
        String action = learner.shouldExplore() 
            ? chooseRandomAction()
            : learner.getBestAction(context);
        
        // Execute action
        boolean success = executeAction(action, context);
        
        // Record action and outcome
        learner.recordAction(action, context);
        learner.recordOutcome(action, success, calculateReward(success));
    }
    
    private String getStateKey(BehaviorContext context) {
        // Create state representation
        float health = context.getEntity().getHealth();
        boolean enemyNearby = hasNearbyEnemy(context);
        
        return String.format("h%.1f_e%b", health, enemyNearby);
    }
    
    private float calculateReward(boolean success) {
        return success ? 1.0f : -0.5f;
    }
}
```

### Action Success Tracking

```java
// Track success rates for each action
Map<String, Float> successRates = learner.getActionSuccessRates();

for (Map.Entry<String, Float> entry : successRates.entrySet()) {
    System.out.println(entry.getKey() + ": " + 
        (entry.getValue() * 100) + "% success");
}

// Get best performing action
String bestAction = learner.getBestAction(context);
float successRate = learner.getSuccessRate(bestAction);
```

### Reward Shaping

```java
private float calculateReward(BehaviorContext context, boolean success) {
    float reward = 0.0f;
    
    if (success) {
        reward += 1.0f;
        
        // Bonus for efficiency
        float healthRemaining = context.getEntity().getHealth();
        reward += healthRemaining * 0.1f;
        
        // Bonus for speed
        long duration = getCombatDuration();
        if (duration < 5000) reward += 0.5f;
    } else {
        reward -= 0.5f;
        
        // Penalty for taking damage
        float damageTaken = getDamageTaken();
        reward -= damageTaken * 0.05f;
    }
    
    return reward;
}
```

---

## Pattern Recognition

### Detecting Player Patterns

```java
import com.gerefloc45.voidapi.api.ml.PatternRecognizer;

public class PatternBasedAI {
    private PatternRecognizer recognizer;
    
    public void setup() {
        recognizer = new PatternRecognizer();
    }
    
    public void observePlayer(PlayerEntity player, BehaviorContext context) {
        // Record player actions
        String action = detectPlayerAction(player);
        if (action != null) {
            recognizer.recordPlayerAction(player, action, context);
        }
    }
    
    public void reactToPatterns(PlayerEntity player, BehaviorContext context) {
        // Detect patterns
        List<Pattern> patterns = recognizer.detectPatterns(player);
        
        for (Pattern pattern : patterns) {
            System.out.println("Detected pattern: " + pattern.getSequence());
            System.out.println("Frequency: " + pattern.getFrequency());
        }
        
        // Predict next action
        String prediction = recognizer.predictNextAction(player);
        if (prediction != null) {
            // Counter predicted action
            executeCounter(prediction, context);
        }
    }
    
    private String detectPlayerAction(PlayerEntity player) {
        // Detect what player is doing
        if (player.isAttacking()) return "attack";
        if (player.isSneaking()) return "sneak";
        if (isPlayerDodging(player)) return "dodge";
        if (isPlayerBlocking(player)) return "block";
        return null;
    }
}
```

### Movement Pattern Analysis

```java
// Track player movement patterns
recognizer.recordPlayerMovement(player, player.getPos());

// Get movement statistics
float avgSpeed = recognizer.getAverageMovementSpeed(player);
Vec3d commonDirection = recognizer.getCommonMovementDirection(player);

// Predict player position
Vec3d predictedPos = recognizer.predictPlayerPosition(player, 2.0f); // 2 seconds ahead
```

### Combat Tendency Analysis

```java
// Analyze player combat style
float aggressionLevel = recognizer.getAggressionLevel(player);
float retreatTendency = recognizer.getRetreatTendency(player);

if (aggressionLevel > 0.7f) {
    // Player is aggressive - use defensive tactics
    useDefensiveTactics(context);
} else if (retreatTendency > 0.6f) {
    // Player retreats often - be more aggressive
    useAggressiveTactics(context);
}
```

---

## Adaptive Difficulty

### Per-Player Difficulty

```java
import com.gerefloc45.voidapi.api.ml.AdaptiveDifficulty;

public class AdaptiveAI {
    private AdaptiveDifficulty difficulty;
    
    public void setup(LivingEntity entity) {
        difficulty = new AdaptiveDifficulty(entity);
    }
    
    public void onCombatEnd(PlayerEntity player, boolean playerWon) {
        // Record combat result
        difficulty.recordCombatEnd(player, playerWon);
        
        // Get player statistics
        float winRate = difficulty.getPlayerWinRate(player);
        float avgCombatDuration = difficulty.getAverageCombatDuration(player);
        
        System.out.println("Player win rate: " + (winRate * 100) + "%");
        System.out.println("Avg combat duration: " + avgCombatDuration + "ms");
    }
    
    public void applyDifficulty(PlayerEntity player, BehaviorContext context) {
        // Get difficulty multipliers
        float reactionTime = difficulty.getReactionTimeMultiplier(player);
        float accuracy = difficulty.getAccuracyMultiplier(player);
        float aggression = difficulty.getAggressionMultiplier(player);
        
        // Apply to AI behavior
        setReactionDelay((int)(baseDelay / reactionTime));
        setAccuracy(baseAccuracy * accuracy);
        setAggressionLevel(baseAggression * aggression);
    }
}
```

### Dynamic Modifiers

```java
// Difficulty adjusts based on player performance
// Player winning too much -> AI gets harder
// Player losing too much -> AI gets easier

float winRate = difficulty.getPlayerWinRate(player);

if (winRate > 0.7f) {
    // Player winning 70%+ -> Increase difficulty
    // reactionTime: 1.2x faster
    // accuracy: 1.15x better
    // aggression: 1.1x more aggressive
} else if (winRate < 0.3f) {
    // Player winning <30% -> Decrease difficulty
    // reactionTime: 0.8x slower
    // accuracy: 0.85x worse
    // aggression: 0.9x less aggressive
}
```

### Health Trend Analysis

```java
// Track player health trends during combat
difficulty.recordPlayerHealth(player, player.getHealth());

// Get health trend
float healthTrend = difficulty.getPlayerHealthTrend(player);

if (healthTrend < -0.5f) {
    // Player losing health quickly - ease up
    reduceAggression();
} else if (healthTrend > 0.3f) {
    // Player maintaining/gaining health - increase pressure
    increaseAggression();
}
```

---

## Training Mode

### Supervised Learning

```java
import com.gerefloc45.voidapi.api.ml.TrainingMode;

public class TrainableAI {
    private TrainingMode training;
    
    public void setup(LivingEntity entity) {
        training = new TrainingMode(entity);
    }
    
    public void startTraining(String taskName) {
        training.startTraining(taskName);
        System.out.println("Training started: " + taskName);
    }
    
    public void recordDemonstration(BehaviorContext context, Behavior behavior) {
        // Record state-action pair
        training.recordDemonstration(context, behavior);
    }
    
    public void endTraining() {
        training.endTraining();
        System.out.println("Training complete!");
        System.out.println("Demonstrations: " + training.getDemonstrationCount());
    }
    
    public Behavior getLearnedBehavior(BehaviorContext context) {
        // Get behavior matching current context
        return training.getLearnedBehavior(context);
    }
}
```

### Training Example: Patrol Route

```java
// 1. Start training
training.startTraining("patrol_route");

// 2. Demonstrate patrol route
for (BlockPos waypoint : patrolRoute) {
    BehaviorContext ctx = createContext(waypoint);
    Behavior moveBehavior = new PathfindingBehavior(waypoint);
    training.recordDemonstration(ctx, moveBehavior);
}

// 3. End training
training.endTraining();

// 4. Use learned behavior
public Status patrol(BehaviorContext context) {
    Behavior learned = training.getLearnedBehavior(context);
    if (learned != null) {
        return learned.tick(context);
    }
    return Status.FAILURE;
}
```

### Training Example: Combat Tactics

```java
training.startTraining("combat_tactics");

// Demonstrate different situations
// Situation 1: Enemy far away
BehaviorContext farContext = createContext("enemy_far");
training.recordDemonstration(farContext, approachBehavior);

// Situation 2: Enemy nearby
BehaviorContext nearContext = createContext("enemy_near");
training.recordDemonstration(nearContext, attackBehavior);

// Situation 3: Low health
BehaviorContext lowHealthContext = createContext("low_health");
training.recordDemonstration(lowHealthContext, fleeBehavior);

training.endTraining();

// AI now knows what to do in each situation
```

---

## Behavior Tree Integration

### LearningNode

```java
import com.gerefloc45.voidapi.api.ml.LearningNode;

// Create learning node with multiple behaviors
LearningNode learner = new LearningNode()
    .addBehavior("attack", new ActionNode(ctx -> {
        // Attack logic
        return Status.SUCCESS;
    }))
    .addBehavior("flee", new ActionNode(ctx -> {
        // Flee logic
        return Status.SUCCESS;
    }))
    .addBehavior("hide", new ActionNode(ctx -> {
        // Hide logic
        return Status.SUCCESS;
    }));

// Add to behavior tree
BehaviorTree tree = new BehaviorTree(
    new SelectorNode()
        .addChild(learner)
        .addChild(defaultBehavior)
);

// Node automatically:
// - Selects best action based on learning
// - Records outcomes
// - Updates Q-values
// - Balances exploration/exploitation
```

### Hybrid Learning System

```java
BehaviorTree tree = new BehaviorTree(
    new SelectorNode()
        // Emergency reactions (no learning)
        .addChild(new ConditionalNode(
            ctx -> ctx.getEntity().getHealth() < 4.0f,
            emergencyFleeBehavior
        ))
        
        // Learned tactical behavior
        .addChild(new LearningNode()
            .addBehavior("aggressive", aggressiveBehavior)
            .addBehavior("defensive", defensiveBehavior)
            .addBehavior("tactical", tacticalBehavior)
        )
        
        // Default behavior
        .addChild(patrolBehavior)
);
```

---

## Data Persistence

### Saving Learning Data

```java
// Learning data automatically saved to NBT
public void writeCustomDataToNbt(NbtCompound nbt) {
    // BehaviorLearner saves Q-values
    learner.writeToNbt(nbt);
    
    // PatternRecognizer saves patterns
    recognizer.writeToNbt(nbt);
    
    // AdaptiveDifficulty saves player profiles
    difficulty.writeToNbt(nbt);
    
    // TrainingMode saves demonstrations
    training.writeToNbt(nbt);
}

public void readCustomDataFromNbt(NbtCompound nbt) {
    learner.readFromNbt(nbt);
    recognizer.readFromNbt(nbt);
    difficulty.readFromNbt(nbt);
    training.readFromNbt(nbt);
}
```

### Data Lifecycle

```java
// Data persists across:
// - Server restarts
// - Entity unload/reload
// - World save/load

// Clear data when needed
learner.reset(); // Clear all learning data
recognizer.clearPlayerData(player); // Clear specific player
difficulty.resetPlayerProfile(player); // Reset difficulty
```

---

## Best Practices

### ✅ Do's

1. **Start with high exploration** - Let AI discover strategies
2. **Shape rewards carefully** - Guide learning direction
3. **Use appropriate learning rates** - 0.1-0.3 for most cases
4. **Combine with other systems** - ML + BT + FSM
5. **Test with real players** - Simulated data isn't enough
6. **Monitor performance** - Track learning progress

### ❌ Don'ts

1. **Don't over-fit** - Keep exploration rate > 0
2. **Don't use huge state spaces** - Keep states manageable
3. **Don't ignore edge cases** - Handle rare situations
4. **Don't update too frequently** - Batch updates when possible
5. **Don't forget to persist** - Save learning data
6. **Don't make difficulty too adaptive** - Keep some consistency

### Performance Tips

```java
// ✅ Good - Batch updates
private List<Experience> experienceBuffer = new ArrayList<>();

public void recordExperience(String action, boolean success, float reward) {
    experienceBuffer.add(new Experience(action, success, reward));
    
    // Update in batches
    if (experienceBuffer.size() >= 10) {
        for (Experience exp : experienceBuffer) {
            learner.recordOutcome(exp.action, exp.success, exp.reward);
        }
        experienceBuffer.clear();
    }
}

// ✅ Good - Limit pattern history
recognizer.setMaxHistorySize(100); // Keep last 100 actions

// ✅ Good - Update intervals
if (world.getTime() % 20 == 0) { // Every second
    updateLearning();
}
```

---

## Examples

### Example 1: Learning Combat AI

```java
public class LearningCombatEntity extends MobEntity {
    private BehaviorLearner learner;
    private BehaviorTree brain;
    
    @Override
    protected void initGoals() {
        learner = new BehaviorLearner(this);
        
        LearningNode combatLearner = new LearningNode()
            .addBehavior("melee", createMeleeBehavior())
            .addBehavior("ranged", createRangedBehavior())
            .addBehavior("defensive", createDefensiveBehavior());
        
        brain = new BehaviorTree(
            new SelectorNode()
                .addChild(combatLearner)
                .addChild(createPatrolBehavior())
        );
        
        BrainController.getInstance().attachBrain(this, brain);
    }
    
    @Override
    public void tick() {
        super.tick();
        
        // AI learns which combat style works best
        // against different players and situations
    }
}
```

### Example 2: Adaptive Boss Fight

```java
public class AdaptiveBoss extends BossEntity {
    private AdaptiveDifficulty difficulty;
    private PatternRecognizer patterns;
    
    @Override
    protected void initGoals() {
        difficulty = new AdaptiveDifficulty(this);
        patterns = new PatternRecognizer();
        
        // Boss adapts to player skill and patterns
    }
    
    @Override
    public void onAttacking(PlayerEntity player) {
        // Record player actions
        patterns.recordPlayerAction(player, "attack", getContext());
        
        // Predict and counter
        String prediction = patterns.predictNextAction(player);
        if (prediction != null) {
            executeCounterStrategy(prediction);
        }
    }
    
    @Override
    public void onDeath() {
        PlayerEntity player = getAttacker();
        if (player != null) {
            difficulty.recordCombatEnd(player, true);
            
            // Next time, boss will be harder for this player
        }
    }
}
```

### Example 3: Trainable Companion

```java
public class TrainableCompanion extends TameableEntity {
    private TrainingMode training;
    
    public void startTeaching(PlayerEntity player, String task) {
        training.startTraining(task);
        player.sendMessage(Text.literal("Teaching " + task + "..."));
    }
    
    public void demonstrateAction(BehaviorContext context, Behavior behavior) {
        if (training.isTraining()) {
            training.recordDemonstration(context, behavior);
        }
    }
    
    public void finishTeaching(PlayerEntity player) {
        training.endTraining();
        player.sendMessage(Text.literal("Training complete! " + 
            training.getDemonstrationCount() + " demonstrations recorded."));
    }
    
    @Override
    public void tick() {
        super.tick();
        
        // Use learned behavior
        Behavior learned = training.getLearnedBehavior(getContext());
        if (learned != null) {
            learned.tick(getContext());
        }
    }
}
```

---

## Next Steps

- **[GOAP Guide](GOAP-Guide)** - Combine ML with planning
- **[FSM Guide](FSM-Guide)** - State-based learning
- **[Examples](Basic-Examples)** - Complete examples
- **[API Reference](API-Core)** - Full API documentation

---

**Need help?** Check the **[FAQ](FAQ)** or open an issue on GitHub!
