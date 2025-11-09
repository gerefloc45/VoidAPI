# Utility AI System - Examples

## Example 1: Combat AI with Utility Scoring

This example shows how to create a smart combat AI that chooses between attacking, fleeing, and taking cover based on multiple factors.

```java
import com.gerefloc45.nemesisapi.api.*;
import com.gerefloc45.nemesisapi.api.nodes.*;
import com.gerefloc45.nemesisapi.api.utility.*;
import com.gerefloc45.nemesisapi.api.perception.*;
import net.minecraft.entity.LivingEntity;

public class SmartCombatAI {
    
    public static BehaviorTree create(LivingEntity entity) {
        // Create sensors
        EntitySensor<LivingEntity> enemySensor = new EntitySensor<>(
            LivingEntity.class, 
            16.0, 
            "nearby_enemies"
        );
        
        // Create behaviors
        Behavior attackBehavior = createAttackBehavior();
        Behavior fleeBehavior = createFleeBehavior();
        Behavior takeCoverBehavior = createTakeCoverBehavior();
        
        // Create utility scorers using Considerations
        Scorer attackScore = createAttackScorer();
        Scorer fleeScore = createFleeScorer();
        Scorer coverScore = createCoverScorer();
        
        // Create utility selector
        UtilitySelector combatSelector = new UtilitySelector(10.0) // Re-evaluate every 0.5 seconds
            .addChild(attackBehavior, attackScore)
            .addChild(fleeBehavior, fleeScore)
            .addChild(takeCoverBehavior, coverScore);
        
        return new BehaviorTree(combatSelector);
    }
    
    // Attack scorer: higher when healthy and enemy is close
    private static Scorer createAttackScorer() {
        // Health consideration (lower health = lower attack score)
        Consideration healthConsideration = Consideration.builder()
            .input(ctx -> (double) ctx.getEntity().getHealth() / ctx.getEntity().getMaxHealth())
            .curve(ResponseCurve.quadratic()) // Emphasize high health
            .range(0.0, 1.0)
            .build();
        
        // Distance consideration (prefer medium range)
        Consideration distanceConsideration = Consideration.builder()
            .input(ctx -> {
                LivingEntity target = ctx.getEntity().getTarget();
                if (target == null) return 1000.0;
                return ctx.getEntity().distanceTo(target);
            })
            .curve(ResponseCurve.inverseQuadratic()) // Closer = better
            .range(0.0, 16.0)
            .build();
        
        // Combine considerations (multiply for conservative scoring)
        return healthConsideration.multiply(distanceConsideration);
    }
    
    // Flee scorer: higher when low health
    private static Scorer createFleeScorer() {
        // Low health = high flee score
        Consideration lowHealthConsideration = Consideration.builder()
            .input(ctx -> (double) ctx.getEntity().getHealth() / ctx.getEntity().getMaxHealth())
            .curve(ResponseCurve.quadratic().invert()) // Invert: low health = high score
            .range(0.0, 1.0)
            .build();
        
        // Enemy count consideration (more enemies = higher flee score)
        Consideration enemyCountConsideration = Consideration.builder()
            .input(ctx -> (double) ctx.getBlackboard().get("nearby_enemies_count").orElse(0))
            .curve(ResponseCurve.linear())
            .range(0.0, 5.0)
            .build();
        
        return lowHealthConsideration.multiply(enemyCountConsideration).scale(1.2);
    }
    
    // Cover scorer: moderate when health is medium
    private static Scorer createCoverScorer() {
        // Medium health = prefer cover
        Consideration healthConsideration = Consideration.builder()
            .input(ctx -> (double) ctx.getEntity().getHealth() / ctx.getEntity().getMaxHealth())
            .curve(ctx -> {
                // Peak at 0.5 (medium health)
                double dist = Math.abs(ctx - 0.5);
                return 1.0 - (dist * 2.0);
            })
            .range(0.0, 1.0)
            .build();
        
        return healthConsideration.scale(0.8);
    }
    
    private static Behavior createAttackBehavior() {
        return new ActionNode(ctx -> {
            LivingEntity target = ctx.getEntity().getTarget();
            if (target != null && ctx.getEntity().distanceTo(target) < 3.0) {
                ctx.getEntity().tryAttack(target);
                return Behavior.Status.SUCCESS;
            }
            return Behavior.Status.RUNNING;
        });
    }
    
    private static Behavior createFleeBehavior() {
        return new ActionNode(ctx -> {
            LivingEntity target = ctx.getEntity().getTarget();
            if (target != null) {
                // Move away from target
                return Behavior.Status.RUNNING;
            }
            return Behavior.Status.SUCCESS;
        });
    }
    
    private static Behavior createTakeCoverBehavior() {
        return new ActionNode(ctx -> {
            // Find nearby cover and move to it
            return Behavior.Status.RUNNING;
        });
    }
}
```

## Example 2: Resource Gathering AI

```java
public class ResourceGathererAI {
    
    public static BehaviorTree create(LivingEntity entity) {
        // Behaviors
        Behavior gatherWood = createGatherBehavior("wood");
        Behavior gatherStone = createGatherBehavior("stone");
        Behavior gatherFood = createGatherBehavior("food");
        
        // Utility scores based on inventory and needs
        Scorer woodScore = ctx -> {
            int woodCount = getInventoryCount(ctx, "wood");
            return 1.0 - (woodCount / 64.0); // Need more when we have less
        };
        
        Scorer stoneScore = ctx -> {
            int stoneCount = getInventoryCount(ctx, "stone");
            return 1.0 - (stoneCount / 64.0);
        };
        
        Scorer foodScore = ctx -> {
            float hunger = ctx.getEntity().getHungerManager().getFoodLevel();
            return (20.0 - hunger) / 20.0; // Higher score when hungry
        };
        
        UtilitySelector resourceSelector = new UtilitySelector(40.0) // Check every 2 seconds
            .addChild(gatherWood, woodScore)
            .addChild(gatherStone, stoneScore)
            .addChild(gatherFood, foodScore);
        
        return new BehaviorTree(resourceSelector);
    }
    
    private static Behavior createGatherBehavior(String resourceType) {
        return new ActionNode(ctx -> {
            // Gather resource logic
            return Behavior.Status.SUCCESS;
        });
    }
    
    private static int getInventoryCount(BehaviorContext ctx, String itemType) {
        // Count items in inventory
        return 0;
    }
}
```

## Example 3: Dynamic Priority for Task Management

```java
public class TaskManagerAI {
    
    public static BehaviorTree create(LivingEntity entity) {
        // Different tasks with dynamic priorities
        Behavior defendBase = createDefendTask();
        Behavior repairBuildings = createRepairTask();
        Behavior gatherResources = createGatherTask();
        Behavior rest = createRestTask();
        
        // Priority scorers
        Scorer defendPriority = ctx -> {
            boolean enemiesNearby = ctx.getBlackboard().get("enemies_nearby").orElse(false);
            return enemiesNearby ? 1.0 : 0.1;
        };
        
        Scorer repairPriority = ctx -> {
            int damagedBuildings = ctx.getBlackboard().get("damaged_buildings").orElse(0);
            return Math.min(1.0, damagedBuildings / 3.0);
        };
        
        Scorer gatherPriority = ctx -> {
            int resourceLevel = ctx.getBlackboard().get("resource_level").orElse(100);
            return 1.0 - (resourceLevel / 100.0);
        };
        
        Scorer restPriority = ctx -> {
            float health = ctx.getEntity().getHealth() / ctx.getEntity().getMaxHealth();
            return health < 0.3 ? 1.0 : 0.0;
        };
        
        // Dynamic priority selector automatically reorders tasks
        DynamicPrioritySelector taskSelector = new DynamicPrioritySelector()
            .addPrioritized(defendBase, defendPriority)
            .addPrioritized(repairBuildings, repairPriority)
            .addPrioritized(gatherResources, gatherPriority)
            .addPrioritized(rest, restPriority);
        
        return new BehaviorTree(taskSelector);
    }
    
    // Task implementations...
}
```

## Response Curve Examples

```java
// Different curve shapes for different use cases

// 1. Binary decision (step function)
ResponseCurve binaryDecision = ResponseCurve.step(0.5); // 0 below 0.5, 1 above

// 2. Gradual increase (linear)
ResponseCurve gradual = ResponseCurve.linear();

// 3. Emphasis on high values (quadratic)
ResponseCurve emphasizeHigh = ResponseCurve.quadratic();

// 4. Emphasis on low values (inverse quadratic)
ResponseCurve emphasizeLow = ResponseCurve.inverseQuadratic();

// 5. Smooth S-curve transition
ResponseCurve smooth = ResponseCurve.logistic(10.0);

// 6. Exponential growth
ResponseCurve explosive = ResponseCurve.exponential(3.0);

// 7. Custom curve
ResponseCurve custom = x -> {
    if (x < 0.3) return 0.0;
    if (x > 0.7) return 1.0;
    return (x - 0.3) / 0.4; // Linear interpolation in middle range
};

// 8. Chained curves
ResponseCurve complex = ResponseCurve.quadratic()
    .then(ResponseCurve.smoothStep())
    .clamp(0.2, 0.9);
```

## Tips for Good Utility AI

### 1. Normalize Inputs
Always normalize raw values to [0, 1] range:
```java
double normalized = (value - min) / (max - min);
```

### 2. Use Appropriate Curves
- **Linear**: Default, balanced response
- **Quadratic**: Emphasize high values, ignore low values
- **Inverse Quadratic**: Emphasize low values
- **Logistic**: Clear threshold with smooth transition
- **Step**: Binary on/off decisions

### 3. Combine Considerations Carefully
- **Multiply**: Conservative (all must be good)
- **Add**: Liberal (any can contribute)
- **Custom**: Complex decision logic

### 4. Tune Re-evaluation Intervals
- Fast-paced combat: 5-10 ticks
- Strategic decisions: 20-40 ticks
- Long-term planning: 60+ ticks

### 5. Debug with Blackboard
Store scores for visualization:
```java
context.getBlackboard().set("attack_score", attackScore);
context.getBlackboard().set("flee_score", fleeScore);
```

## Performance Considerations

- Utility calculations run every frame (or per interval)
- Keep scorers simple and fast
- Cache expensive calculations in blackboard
- Use appropriate re-evaluation intervals
- Consider using DynamicPriority for fewer behaviors
