# Goal-Oriented Action Planning (GOAP)

GOAP is a powerful AI planning system where entities dynamically create sequences of actions to achieve goals based on the current world state.

## 📚 Table of Contents

- [What is GOAP?](#what-is-goap)
- [Core Concepts](#core-concepts)
- [Getting Started](#getting-started)
- [Creating Actions](#creating-actions)
- [Defining Goals](#defining-goals)
- [Planning System](#planning-system)
- [Behavior Tree Integration](#behavior-tree-integration)
- [Advanced Usage](#advanced-usage)
- [Best Practices](#best-practices)
- [Examples](#examples)

---

## What is GOAP?

**Goal-Oriented Action Planning** is an AI architecture where:
- Entities have **goals** they want to achieve
- **Actions** have preconditions and effects
- A **planner** finds the optimal sequence of actions
- Plans are executed step-by-step

### Why Use GOAP?

✅ **Flexible** - AI adapts to changing situations  
✅ **Emergent** - Complex behavior from simple actions  
✅ **Maintainable** - Easy to add new actions/goals  
✅ **Efficient** - A* algorithm finds optimal plans  

### When to Use GOAP

- Multi-step problem solving
- Dynamic world states
- Complex decision making
- Emergent AI behavior

---

## Core Concepts

### 1. WorldState

Represents the current state of the world using key-value pairs.

```java
WorldState state = new WorldState();
state.set("hasWeapon", true);
state.set("enemyNearby", false);
state.set("healthLow", false);
```

### 2. Action

Defines what an entity can do, with preconditions and effects.

```java
public class AttackAction extends Action {
    public AttackAction() {
        super("Attack", 1.0f); // Name and base cost
        
        // What must be true to execute
        preconditions.set("hasWeapon", true);
        preconditions.set("enemyNearby", true);
        
        // What changes after execution
        effects.set("enemyAlive", false);
    }
    
    @Override
    public Status execute(BehaviorContext context) {
        // Attack logic here
        return Status.SUCCESS;
    }
}
```

### 3. Goal

Defines what the entity wants to achieve.

```java
WorldState goalState = new WorldState();
goalState.set("enemyAlive", false);

Goal goal = new Goal("KillEnemy", goalState, 10.0f); // Priority 10
```

### 4. Planner

Uses A* algorithm to find the optimal action sequence.

```java
Planner planner = new Planner();
Plan plan = planner.plan(context, currentState, goal, availableActions);
```

### 5. Plan

Ordered sequence of actions with total cost.

```java
if (plan != null) {
    System.out.println("Plan cost: " + plan.getTotalCost());
    for (Action action : plan.getActions()) {
        System.out.println("- " + action.getName());
    }
}
```

---

## Getting Started

### Basic GOAP Setup

```java
import com.gerefloc45.voidapi.api.goap.*;
import com.gerefloc45.voidapi.api.goap.actions.*;

// 1. Define current world state
WorldState currentState = new WorldState();
currentState.set("hasWeapon", false);
currentState.set("enemyNearby", true);
currentState.set("enemyAlive", true);

// 2. Define goal
WorldState goalState = new WorldState();
goalState.set("enemyAlive", false);
Goal goal = new Goal("KillEnemy", goalState, 10.0f);

// 3. Create available actions
List<Action> actions = new ArrayList<>();
actions.add(new GetWeaponAction());
actions.add(new AttackAction());

// 4. Plan
Planner planner = new Planner();
Plan plan = planner.plan(context, currentState, goal, actions);

// 5. Execute
if (plan != null) {
    PlanExecutor executor = new PlanExecutor(plan);
    Status status = executor.execute(context);
}
```

---

## Creating Actions

### Simple Action

```java
public class GetWeaponAction extends Action {
    public GetWeaponAction() {
        super("GetWeapon", 2.0f); // Higher cost than attack
        
        preconditions.set("hasWeapon", false);
        effects.set("hasWeapon", true);
    }
    
    @Override
    public Status execute(BehaviorContext context) {
        LivingEntity entity = context.getEntity();
        
        // Find and equip weapon
        ItemStack weapon = findNearbyWeapon(entity);
        if (weapon != null) {
            entity.equipStack(EquipmentSlot.MAINHAND, weapon);
            return Status.SUCCESS;
        }
        
        return Status.FAILURE;
    }
}
```

### Action with Procedural Cost

Cost can vary based on context:

```java
public class MoveToPositionAction extends Action {
    private final String targetKey;
    
    public MoveToPositionAction(String targetKey) {
        super("MoveTo", 1.0f);
        this.targetKey = targetKey;
        
        preconditions.set("canMove", true);
        effects.set("atTarget", true);
    }
    
    @Override
    public float getCost(BehaviorContext context) {
        // Cost based on distance
        Vec3d target = context.getBlackboard()
            .<Vec3d>get(targetKey).orElse(null);
        if (target == null) return Float.MAX_VALUE;
        
        double distance = context.getEntity().getPos().distanceTo(target);
        return (float) (baseCost + distance * 0.1f);
    }
    
    @Override
    public Status execute(BehaviorContext context) {
        // Pathfinding logic
        return Status.RUNNING;
    }
}
```

### Action Lifecycle

```java
public class ComplexAction extends Action {
    private long startTime;
    
    @Override
    public void onStart(BehaviorContext context) {
        // Called once when action starts
        startTime = System.currentTimeMillis();
        context.getBlackboard().set("action_started", true);
    }
    
    @Override
    public Status execute(BehaviorContext context) {
        // Called every tick while running
        long elapsed = System.currentTimeMillis() - startTime;
        
        if (elapsed > 5000) {
            return Status.SUCCESS;
        }
        
        return Status.RUNNING;
    }
    
    @Override
    public void onEnd(BehaviorContext context) {
        // Called when action completes or is cancelled
        context.getBlackboard().remove("action_started");
    }
}
```

---

## Defining Goals

### Static Goal

```java
WorldState goalState = new WorldState();
goalState.set("enemyAlive", false);
goalState.set("safe", true);

Goal goal = new Goal("BeSafe", goalState, 10.0f);
```

### Dynamic Goal Priority

```java
public class DynamicGoal extends Goal {
    @Override
    public float getRelevance(BehaviorContext context) {
        // Higher priority when health is low
        float health = context.getEntity().getHealth();
        float maxHealth = context.getEntity().getMaxHealth();
        float healthPercent = health / maxHealth;
        
        return priority * (1.0f - healthPercent);
    }
}
```

### Multiple Goals

```java
List<Goal> goals = new ArrayList<>();
goals.add(new Goal("KillEnemy", killState, 10.0f));
goals.add(new Goal("Heal", healState, 15.0f)); // Higher priority
goals.add(new Goal("Patrol", patrolState, 5.0f));

// Planner selects highest priority achievable goal
Goal selectedGoal = selectBestGoal(goals, currentState);
```

---

## Planning System

### A* Planning

The planner uses A* algorithm to find optimal plans:

```java
Planner planner = new Planner();
planner.setMaxNodes(1000); // Prevent infinite loops

Plan plan = planner.plan(context, currentState, goal, actions);

if (plan == null) {
    // No plan found - goal unreachable
    System.out.println("Cannot achieve goal!");
} else {
    System.out.println("Plan found with cost: " + plan.getTotalCost());
}
```

### Plan Execution

```java
PlanExecutor executor = new PlanExecutor(plan);

// Execute plan step by step
while (!executor.isComplete()) {
    Status status = executor.execute(context);
    
    if (status == Status.FAILURE) {
        // Current action failed - replan
        break;
    }
}
```

### Replanning

Replan when world state changes:

```java
public class SmartGOAPController {
    private Plan currentPlan;
    private PlanExecutor executor;
    private WorldState lastState;
    
    public Status tick(BehaviorContext context) {
        WorldState currentState = getWorldState(context);
        
        // Replan if state changed significantly
        if (shouldReplan(currentState, lastState)) {
            currentPlan = planner.plan(context, currentState, goal, actions);
            executor = new PlanExecutor(currentPlan);
        }
        
        lastState = currentState.copy();
        return executor.execute(context);
    }
    
    private boolean shouldReplan(WorldState current, WorldState last) {
        // Check if important state changed
        return !current.equals(last);
    }
}
```

---

## Behavior Tree Integration

### GOAPNode

Integrate GOAP into behavior trees:

```java
import com.gerefloc45.voidapi.api.goap.GOAPNode;

// Create GOAP node
GOAPNode goapNode = new GOAPNode(
    goal,
    actions,
    context -> getWorldState(context) // State provider
);

// Add to behavior tree
BehaviorTree tree = new BehaviorTree(
    new SelectorNode()
        .addChild(goapNode)
        .addChild(fallbackBehavior)
);
```

### Automatic Replanning

```java
GOAPNode goapNode = new GOAPNode(goal, actions, stateProvider);
goapNode.setReplanInterval(20); // Replan every 20 ticks

// Node automatically replans when:
// - World state changes
// - Current plan fails
// - Replan interval elapsed
```

### Hybrid AI

Combine GOAP with other systems:

```java
BehaviorTree tree = new BehaviorTree(
    new SelectorNode()
        // Emergency reactions (FSM)
        .addChild(new StateMachineNode(emergencyFSM))
        
        // Tactical planning (GOAP)
        .addChild(new GOAPNode(tacticalGoal, tacticalActions, stateProvider))
        
        // Default behavior (BT)
        .addChild(new SequenceNode()
            .addChild(patrolBehavior)
            .addChild(idleBehavior)
        )
);
```

---

## Advanced Usage

### Complex World State

```java
public class ComplexWorldState {
    public static WorldState create(BehaviorContext context) {
        WorldState state = new WorldState();
        LivingEntity entity = context.getEntity();
        
        // Health
        float healthPercent = entity.getHealth() / entity.getMaxHealth();
        state.set("healthLow", healthPercent < 0.3f);
        state.set("healthCritical", healthPercent < 0.1f);
        
        // Combat
        LivingEntity target = findTarget(entity);
        state.set("hasTarget", target != null);
        state.set("targetNearby", target != null && 
            entity.distanceTo(target) < 16.0);
        
        // Inventory
        state.set("hasWeapon", hasWeapon(entity));
        state.set("hasFood", hasFood(entity));
        state.set("hasPotion", hasPotion(entity));
        
        // Environment
        state.set("inDanger", isInDanger(entity));
        state.set("canHide", canFindHidingSpot(entity));
        
        return state;
    }
}
```

### Action Chains

Create complex behaviors from simple actions:

```java
// Actions
GetWeaponAction getWeapon = new GetWeaponAction();
ApproachEnemyAction approach = new ApproachEnemyAction();
AttackAction attack = new AttackAction();
FleeAction flee = new FleeAction();

// Goals
Goal killEnemy = new Goal("Kill", killState, 10.0f);
Goal survive = new Goal("Survive", surviveState, 20.0f);

// Planner automatically chains:
// GetWeapon -> Approach -> Attack (if healthy)
// Flee (if low health)
```

### State Providers

Reusable state providers:

```java
public interface StateProvider {
    WorldState getState(BehaviorContext context);
}

public class CombatStateProvider implements StateProvider {
    @Override
    public WorldState getState(BehaviorContext context) {
        WorldState state = new WorldState();
        // Combat-specific state
        return state;
    }
}

// Use in GOAPNode
GOAPNode node = new GOAPNode(goal, actions, new CombatStateProvider());
```

---

## Best Practices

### ✅ Do's

1. **Keep actions atomic** - One clear purpose per action
2. **Use procedural costs** - Reflect actual difficulty
3. **Replan frequently** - React to changes
4. **Limit planning depth** - Set max nodes (1000-2000)
5. **Cache plans** - Reuse when state unchanged
6. **Test edge cases** - Unreachable goals, no actions

### ❌ Don'ts

1. **Don't make actions too complex** - Keep them simple
2. **Don't ignore costs** - They guide planning
3. **Don't plan every tick** - Use intervals
4. **Don't forget preconditions** - Validate before execution
5. **Don't hardcode state** - Use dynamic providers

### Performance Tips

```java
// ✅ Good - Replan interval
goapNode.setReplanInterval(20); // Every second

// ✅ Good - Limit nodes
planner.setMaxNodes(1000);

// ✅ Good - Cache state
private WorldState cachedState;
private long lastUpdate;

public WorldState getState(BehaviorContext context) {
    long now = context.getWorld().getTime();
    if (now - lastUpdate > 20) {
        cachedState = buildState(context);
        lastUpdate = now;
    }
    return cachedState;
}
```

---

## Examples

### Example 1: Combat AI

```java
// Actions
public class GetWeaponAction extends Action {
    public GetWeaponAction() {
        super("GetWeapon", 5.0f);
        preconditions.set("hasWeapon", false);
        effects.set("hasWeapon", true);
    }
}

public class AttackAction extends Action {
    public AttackAction() {
        super("Attack", 1.0f);
        preconditions.set("hasWeapon", true);
        preconditions.set("enemyNearby", true);
        effects.set("enemyAlive", false);
    }
}

// Setup
WorldState state = new WorldState();
state.set("hasWeapon", false);
state.set("enemyNearby", true);
state.set("enemyAlive", true);

Goal goal = new Goal("KillEnemy", 
    new WorldState().set("enemyAlive", false), 10.0f);

List<Action> actions = Arrays.asList(
    new GetWeaponAction(),
    new AttackAction()
);

Plan plan = planner.plan(context, state, goal, actions);
// Result: GetWeapon -> Attack
```

### Example 2: Resource Gathering

```java
public class MoveToResourceAction extends Action {
    public MoveToResourceAction() {
        super("MoveToResource", 2.0f);
        preconditions.set("knowsResourceLocation", true);
        effects.set("atResource", true);
    }
}

public class GatherAction extends Action {
    public GatherAction() {
        super("Gather", 3.0f);
        preconditions.set("atResource", true);
        preconditions.set("hasInventorySpace", true);
        effects.set("hasResource", true);
    }
}

public class ReturnHomeAction extends Action {
    public ReturnHomeAction() {
        super("ReturnHome", 2.0f);
        preconditions.set("hasResource", true);
        effects.set("resourceDelivered", true);
    }
}

// Goal: Deliver resources
Goal goal = new Goal("DeliverResources",
    new WorldState().set("resourceDelivered", true), 10.0f);

// Plan: MoveToResource -> Gather -> ReturnHome
```

### Example 3: Survival AI

```java
// Multiple goals with priorities
Goal heal = new Goal("Heal",
    new WorldState().set("healthFull", true), 20.0f); // Highest

Goal findFood = new Goal("FindFood",
    new WorldState().set("hasFood", true), 15.0f);

Goal explore = new Goal("Explore",
    new WorldState().set("areaExplored", true), 5.0f); // Lowest

// AI automatically prioritizes based on current state
// Low health -> Heal
// Hungry -> FindFood
// Otherwise -> Explore
```

---

## Next Steps

- **[FSM Guide](FSM-Guide)** - Combine with state machines
- **[Machine Learning](ML-Guide)** - Add learning to GOAP
- **[Examples](Basic-Examples)** - More complete examples
- **[API Reference](API-Core)** - Full API documentation

---

**Need help?** Check the **[FAQ](FAQ)** or open an issue on GitHub!
