# 🚀 Getting Started with Nemesis-API Framework

Welcome! This guide will help you start using Nemesis-API to create advanced AI for your Minecraft entities in just a few minutes.

---

## 📋 Table of Contents

1. [Prerequisites](#prerequisites)
2. [Installation](#installation)
3. [Your First AI (5 minutes)](#your-first-ai-5-minutes)
4. [Understanding the Basics](#understanding-the-basics)
5. [Step-by-Step Tutorial](#step-by-step-tutorial)
6. [Common Patterns](#common-patterns)
7. [Troubleshooting](#troubleshooting)
8. [Next Steps](#next-steps)

---

## Prerequisites

Before starting, make sure you have:

- ✅ Minecraft 1.21.1
- ✅ Fabric Loader 0.15+
- ✅ Fabric API
- ✅ Java 17+
- ✅ Basic knowledge of Java and Minecraft modding

---

## Installation

### Step 1: Add Nemesis-API to Your Project

Add to your `build.gradle`:

```gradle
repositories {
    mavenLocal() // If you built Nemesis-API locally
}

dependencies {
    modImplementation "com.myname:nemesis-api:0.1.0"
    include "com.myname:nemesis-api:0.1.0"
}
```

### Step 2: Add Dependency in fabric.mod.json

```json
{
  "depends": {
    "nemesisapi": ">=0.1.0"
  }
}
```

### Step 3: Build Your Project

```bash
./gradlew build
```

---

## Your First AI (5 minutes)

Let's create a simple AI that makes a zombie wander around and attack nearby players.

### Complete Example

```java
package com.yourmod.ai;

import com.myname.nemesisapi.api.*;
import com.myname.nemesisapi.api.nodes.*;
import com.myname.nemesisapi.core.*;
import com.myname.nemesisapi.util.EntityUtil;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.Optional;

public class SimpleZombieAI {
    
    public static void attachToZombie(ZombieEntity zombie) {
        // Create behavior tree
        BehaviorTree brain = new BehaviorTree(
            new SelectorNode()
                // Priority 1: Attack if player nearby
                .addChild(new SequenceNode()
                    .addChild(ActionNode.of(ctx -> {
                        // Find nearest player
                        Optional<PlayerEntity> player = 
                            EntityUtil.findNearestPlayer(ctx.getEntity(), 10.0);
                        
                        if (player.isPresent()) {
                            ctx.getBlackboard().set("target", player.get());
                            return Behavior.Status.SUCCESS;
                        }
                        return Behavior.Status.FAILURE;
                    }))
                    .addChild(ActionNode.of(ctx -> {
                        // Attack target
                        PlayerEntity target = ctx.getBlackboard()
                            .get("target").orElse(null);
                        
                        if (target != null && target.isAlive()) {
                            ctx.getEntity().lookAt(target, 30.0f, 30.0f);
                            // Add your attack logic here
                            return Behavior.Status.SUCCESS;
                        }
                        return Behavior.Status.FAILURE;
                    }))
                )
                
                // Priority 2: Wander around
                .addChild(ActionNode.of(ctx -> {
                    // Add your wander logic here
                    return Behavior.Status.SUCCESS;
                }))
        );
        
        // Attach brain to zombie
        BrainController.getInstance().attachBrain(zombie, brain);
        BrainTicker.registerEntity(zombie);
    }
}
```

### How to Use It

In your entity spawn event or entity initialization:

```java
@Override
public void onInitialize() {
    ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
        if (entity instanceof ZombieEntity zombie) {
            SimpleZombieAI.attachToZombie(zombie);
        }
    });
}
```

**That's it!** Your zombie now has AI. 🎉

---

## Understanding the Basics

### 🌳 Behavior Trees

A behavior tree is like a flowchart for AI decisions:

```
Selector (OR - try until one succeeds)
├── Sequence (AND - do all in order)
│   ├── Find Enemy
│   └── Attack Enemy
└── Wander (fallback)
```

### 📝 Key Concepts

#### 1. **Behavior Status**
Every behavior returns a status:
- `SUCCESS` - Task completed successfully
- `FAILURE` - Task failed
- `RUNNING` - Task is still executing (continues next tick)

#### 2. **Nodes**
- **SelectorNode**: Tries children until one succeeds (OR logic)
- **SequenceNode**: Executes children until one fails (AND logic)
- **ActionNode**: Leaf node that does actual work

#### 3. **Blackboard**
Shared memory for storing data between behaviors:
```java
ctx.getBlackboard().set("target", enemy);
PlayerEntity target = ctx.getBlackboard().get("target").orElse(null);
```

#### 4. **Context**
Contains everything a behavior needs:
- `getEntity()` - The entity running the AI
- `getWorld()` - The server world
- `getBlackboard()` - Shared memory
- `getDeltaTime()` - Time since last tick

---

## Step-by-Step Tutorial

### Tutorial 1: Guard AI

Create an AI that guards an area and attacks intruders.

#### Step 1: Define the Behavior Tree Structure

```java
Selector
├── Sequence (Attack if enemy nearby)
│   ├── Find Enemy
│   └── Attack Enemy
├── Sequence (Return to guard post)
│   ├── Check if far from post
│   └── Move to post
└── Idle
```

#### Step 2: Implement Each Behavior

```java
public class GuardAI {
    
    public static BehaviorTree createGuardBrain(Vec3d guardPost) {
        return new BehaviorTree(
            new SelectorNode()
                // Attack behavior
                .addChild(createAttackBehavior())
                // Return to post behavior
                .addChild(createReturnBehavior(guardPost))
                // Idle behavior
                .addChild(createIdleBehavior())
        );
    }
    
    private static Behavior createAttackBehavior() {
        return new SequenceNode()
            .addChild(ActionNode.of(ctx -> {
                // Find nearest enemy
                Optional<PlayerEntity> enemy = 
                    EntityUtil.findNearestPlayer(ctx.getEntity(), 16.0);
                
                if (enemy.isPresent()) {
                    ctx.getBlackboard().set("target", enemy.get());
                    return Behavior.Status.SUCCESS;
                }
                return Behavior.Status.FAILURE;
            }))
            .addChild(new CooldownNode(
                ActionNode.of(ctx -> {
                    PlayerEntity target = ctx.getBlackboard()
                        .get("target").orElse(null);
                    
                    if (target != null && target.isAlive()) {
                        // Attack logic
                        ctx.getEntity().lookAt(target, 30.0f, 30.0f);
                        return Behavior.Status.SUCCESS;
                    }
                    return Behavior.Status.FAILURE;
                }),
                1.0f // 1 second cooldown
            ));
    }
    
    private static Behavior createReturnBehavior(Vec3d guardPost) {
        return new SequenceNode()
            .addChild(ActionNode.of(ctx -> {
                double distance = ctx.getEntity().getPos()
                    .distanceTo(guardPost);
                
                if (distance > 5.0) {
                    ctx.getBlackboard().set("return_pos", guardPost);
                    return Behavior.Status.SUCCESS;
                }
                return Behavior.Status.FAILURE;
            }))
            .addChild(ActionNode.of(ctx -> {
                Vec3d pos = ctx.getBlackboard()
                    .get("return_pos").orElse(null);
                
                if (pos != null) {
                    // Move towards guard post
                    return Behavior.Status.RUNNING;
                }
                return Behavior.Status.SUCCESS;
            }));
    }
    
    private static Behavior createIdleBehavior() {
        return ActionNode.of(ctx -> {
            // Look around randomly
            return Behavior.Status.SUCCESS;
        });
    }
}
```

#### Step 3: Attach to Entity

```java
Vec3d guardPost = new Vec3d(100, 64, 100);
BehaviorTree brain = GuardAI.createGuardBrain(guardPost);
BrainController.getInstance().attachBrain(entity, brain);
BrainTicker.registerEntity(entity);
```

---

### Tutorial 2: Using Sensors (Advanced)

Sensors automatically detect entities and update the blackboard.

```java
import com.myname.nemesisapi.api.perception.*;

public class AdvancedGuardAI {
    
    public static void setupWithSensors(LivingEntity guard) {
        // Create sensor manager
        SensorManager sensors = new SensorManager();
        
        // Add enemy sensor
        EntitySensor<PlayerEntity> enemySensor = new EntitySensor<>(
            PlayerEntity.class,
            20.0,           // 20 block range
            "enemies",      // blackboard key
            true,           // require line of sight
            player -> !player.isCreative(), // filter
            10              // update every 10 ticks (0.5 seconds)
        );
        sensors.addSensor(enemySensor);
        
        // Create memory system
        PerceptionMemory memory = new PerceptionMemory(15.0f);
        
        // Create behavior tree
        BehaviorTree brain = new BehaviorTree(
            new SelectorNode()
                .addChild(new SequenceNode()
                    // Update sensors
                    .addChild(ActionNode.of(ctx -> {
                        sensors.update(ctx);
                        memory.update();
                        return Behavior.Status.SUCCESS;
                    }))
                    // Check for enemies
                    .addChild(ActionNode.of(ctx -> {
                        PlayerEntity nearest = enemySensor.getNearestEntity(ctx);
                        
                        if (nearest != null) {
                            ctx.getBlackboard().set("target", nearest);
                            memory.remember(nearest, 0.9f); // High threat
                            return Behavior.Status.SUCCESS;
                        }
                        
                        // Check memory for last known position
                        List<PerceptionMemory.MemoryEntry> memories = 
                            memory.getMemoriesByThreat();
                        
                        if (!memories.isEmpty()) {
                            Vec3d lastPos = memories.get(0).getLastKnownPosition();
                            ctx.getBlackboard().set("investigate_pos", lastPos);
                            return Behavior.Status.SUCCESS;
                        }
                        
                        return Behavior.Status.FAILURE;
                    }))
                    // Attack or investigate
                    .addChild(ActionNode.of(ctx -> {
                        // Your combat logic
                        return Behavior.Status.SUCCESS;
                    }))
                )
                // Fallback: patrol
                .addChild(ActionNode.of(ctx -> {
                    // Patrol logic
                    return Behavior.Status.SUCCESS;
                }))
        );
        
        // Attach everything
        BrainController.getInstance().attachBrain(guard, brain);
        BrainTicker.registerEntity(guard);
        
        // Store sensors and memory in blackboard
        Blackboard bb = BrainController.getInstance().getBlackboard(guard);
        bb.set("sensors", sensors);
        bb.set("memory", memory);
    }
}
```

---

## Common Patterns

### Pattern 1: Conditional Execution

Execute behavior only when a condition is met:

```java
new ConditionalNode(
    ctx -> ctx.getEntity().getHealth() < 10.0f,
    fleeBehavior
)
```

### Pattern 2: Cooldowns

Prevent spam by adding cooldowns:

```java
new CooldownNode(attackBehavior, 2.0f) // 2 second cooldown
```

### Pattern 3: Repeating Actions

Repeat a behavior multiple times:

```java
new RepeatNode(patrolBehavior, 5) // Patrol 5 times
```

### Pattern 4: Parallel Execution

Do multiple things at once:

```java
new ParallelNode(ParallelNode.Policy.REQUIRE_ALL)
    .addChild(moveBehavior)
    .addChild(soundBehavior)
    .addChild(particleBehavior)
```

### Pattern 5: Inverting Results

Invert SUCCESS/FAILURE:

```java
new InverterNode(isEnemyNearby) // True becomes false
```

---

## Troubleshooting

### Problem: AI not updating

**Solution**: Make sure you registered the entity:
```java
BrainTicker.registerEntity(entity);
```

### Problem: Blackboard data not persisting

**Solution**: Blackboard is per-entity. Make sure you're accessing the correct entity's blackboard:
```java
Blackboard bb = BrainController.getInstance().getBlackboard(entity);
```

### Problem: Behaviors not executing

**Solution**: Check your tree structure. Selector stops at first success, Sequence stops at first failure:
```java
// This will NEVER reach behavior2 if behavior1 succeeds
new SelectorNode()
    .addChild(behavior1) // Always succeeds
    .addChild(behavior2) // Never executed!
```

### Problem: Performance issues

**Solution**: 
1. Use sensor update frequencies (don't update every tick)
2. Use cooldowns to limit expensive operations
3. Consider async for heavy calculations

```java
// Good: Update every 10 ticks
new EntitySensor<>(..., 10)

// Good: Cooldown on expensive pathfinding
new CooldownNode(pathfindingBehavior, 2.0f)
```

---

## Next Steps

### 📚 Learn More

1. **[EXAMPLES.md](EXAMPLES.md)** - Complete practical examples
2. **[AI-ROADMAP.md](AI-ROADMAP.md)** - Future features and roadmap
3. **[README-NEMESIS-API.md](README-NEMESIS-API.md)** - Full API documentation

### 🎯 Try These Challenges

1. **Easy**: Create a fleeing AI that runs from players
2. **Medium**: Create a patrol AI that follows waypoints
3. **Hard**: Create a pack AI where entities coordinate
4. **Expert**: Create a learning AI that adapts to player behavior

### 🔧 Advanced Topics

- **Utility AI**: Score-based decision making (coming in v1.2.0)
- **GOAP**: Goal-oriented action planning (coming in v1.3.0)
- **Debug Tools**: Visualize behavior trees (coming in v1.4.0)

---

## 💡 Tips and Best Practices

### ✅ DO

- ✅ Use descriptive blackboard keys: `"target_enemy"` not `"t"`
- ✅ Add cooldowns to expensive operations
- ✅ Use sensors for perception instead of manual checks
- ✅ Keep behaviors small and focused
- ✅ Test behaviors individually before combining

### ❌ DON'T

- ❌ Don't update sensors every tick if not needed
- ❌ Don't forget to unregister entities when removed
- ❌ Don't create new behavior trees every tick
- ❌ Don't block the main thread with heavy calculations
- ❌ Don't forget to handle null values from blackboard

---

## 🤝 Need Help?

- 📖 Check the [examples](EXAMPLES.md)
- 🐛 [Report issues](https://github.com/Gerefloc45/Nemesis-API/issues)
- 💬 [Join our Discord](https://discord.gg/yourserver)
- 📧 Contact: your.email@example.com

---

## 🎉 You're Ready!

You now know the basics of Nemesis-API! Start creating amazing AI for your entities.

**Happy coding!** 🚀

---

**Version**: 0.1.0-beta  
**Last Updated**: 2025-11-08  
**Minecraft**: 1.21.1  
**Java**: 17+
