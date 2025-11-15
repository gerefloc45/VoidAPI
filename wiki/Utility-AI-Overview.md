# Utility AI Overview

Utility AI is a powerful decision-making system that chooses actions based on **scores** rather than fixed priorities. Perfect for complex, context-dependent behavior.

## What is Utility AI?

Instead of "if health < 30% then flee", Utility AI asks:
- "How good is fleeing right now?" → Score: 0.8
- "How good is attacking right now?" → Score: 0.3
- "How good is taking cover right now?" → Score: 0.6

**Choose the highest score!** → Flee (0.8)

## Why Use Utility AI?

### Traditional Approach (Rigid)
```java
if (health < 30%) {
    flee();
} else if (enemyNearby && health > 50%) {
    attack();
} else {
    patrol();
}
```

**Problems**:
- Hard thresholds (why 30%? why not 29%?)
- Doesn't consider multiple factors
- Difficult to tune and balance

### Utility AI Approach (Flexible)
```java
UtilitySelector selector = new UtilitySelector()
    .addChild(fleeBehavior, fleeScore)
    .addChild(attackBehavior, attackScore)
    .addChild(patrolBehavior, patrolScore);
```

**Benefits**:
- ✅ Smooth transitions
- ✅ Considers multiple factors
- ✅ Easy to tune with curves
- ✅ Emergent behavior

## Core Components

### 1. Scorer

Evaluates how good a behavior is:

```java
Scorer attackScore = ctx -> {
    double health = ctx.getEntity().getHealth() / ctx.getEntity().getMaxHealth();
    double distance = ctx.getEntity().distanceTo(target) / 16.0;
    
    return health * (1.0 - distance); // High health + close = high score
};
```

### 2. Consideration

Evaluates a single variable:

```java
Consideration healthConsideration = Consideration.builder()
    .input(ctx -> (double) ctx.getEntity().getHealth() / ctx.getEntity().getMaxHealth())
    .curve(ResponseCurve.quadratic())
    .range(0.0, 1.0)
    .build();
```

### 3. Response Curve

Shapes how input affects output:

```java
ResponseCurve emphasizeHigh = ResponseCurve.quadratic();
// Input 0.5 → Output 0.25
// Input 0.7 → Output 0.49
// Input 0.9 → Output 0.81
```

### 4. UtilitySelector

Chooses behavior with highest score:

```java
UtilitySelector selector = new UtilitySelector(20.0) // Re-evaluate every second
    .addChild(attackBehavior, attackScore)
    .addChild(fleeBehavior, fleeScore)
    .addChild(healBehavior, healScore);
```

## Quick Example

```java
// Health consideration: low health = high flee score
Consideration lowHealth = Consideration.builder()
    .input(ctx -> (double) ctx.getEntity().getHealth() / ctx.getEntity().getMaxHealth())
    .curve(ResponseCurve.quadratic().invert()) // Invert: low = high
    .build();

// Enemy count: more enemies = higher flee score
Consideration manyEnemies = Consideration.builder()
    .input(ctx -> (double) ctx.getBlackboard().get("enemy_count").orElse(0))
    .curve(ResponseCurve.linear())
    .range(0.0, 5.0)
    .build();

// Combine: multiply for conservative scoring
Scorer fleeScore = lowHealth.multiply(manyEnemies);

// Use in selector
UtilitySelector combatAI = new UtilitySelector()
    .addChild(attackBehavior, attackScore)
    .addChild(fleeBehavior, fleeScore)
    .addChild(defendBehavior, defendScore);
```

## Response Curves

Curves shape how inputs map to outputs:

### Linear
```java
ResponseCurve.linear()
```
- Input 0.5 → Output 0.5
- Balanced, no emphasis

### Quadratic
```java
ResponseCurve.quadratic()
```
- Input 0.5 → Output 0.25
- Emphasizes high values

### Inverse Quadratic
```java
ResponseCurve.inverseQuadratic()
```
- Input 0.5 → Output 0.75
- Emphasizes low values

### Logistic (S-Curve)
```java
ResponseCurve.logistic(10.0)
```
- Smooth transition around midpoint
- Great for thresholds

### Custom
```java
ResponseCurve custom = x -> {
    if (x < 0.3) return 0.0;
    if (x > 0.7) return 1.0;
    return (x - 0.3) / 0.4;
};
```

## Combining Considerations

### Multiply (Conservative)
All must be good:
```java
Scorer score = health.multiply(distance).multiply(ammo);
// If any is low, total score is low
```

### Add (Liberal)
Any can contribute:
```java
Scorer score = health.add(distance).add(ammo);
// High values in any boost total score
```

### Custom Logic
```java
Scorer score = ctx -> {
    double h = health.score(ctx);
    double d = distance.score(ctx);
    double a = ammo.score(ctx);
    
    // Custom formula
    return (h * 0.5) + (d * 0.3) + (a * 0.2);
};
```

## Real-World Example: Combat AI

```java
public class SmartCombatAI {
    
    public static BehaviorTree create() {
        // Behaviors
        Behavior attack = new AttackBehavior();
        Behavior flee = new FleeBehavior();
        Behavior cover = new TakeCoverBehavior();
        
        // Attack score: high when healthy and enemy close
        Scorer attackScore = createAttackScorer();
        
        // Flee score: high when low health or many enemies
        Scorer fleeScore = createFleeScorer();
        
        // Cover score: moderate when medium health
        Scorer coverScore = createCoverScorer();
        
        // Utility selector
        UtilitySelector combatAI = new UtilitySelector(10.0)
            .addChild(attack, attackScore)
            .addChild(flee, fleeScore)
            .addChild(cover, coverScore);
        
        return new BehaviorTree(combatAI);
    }
    
    private static Scorer createAttackScorer() {
        Consideration health = Consideration.builder()
            .input(ctx -> (double) ctx.getEntity().getHealth() / ctx.getEntity().getMaxHealth())
            .curve(ResponseCurve.quadratic()) // Prefer high health
            .build();
        
        Consideration distance = Consideration.builder()
            .input(ctx -> {
                LivingEntity target = ctx.getEntity().getTarget();
                return target != null ? ctx.getEntity().distanceTo(target) : 100.0;
            })
            .curve(ResponseCurve.inverseQuadratic()) // Prefer close
            .range(0.0, 16.0)
            .build();
        
        return health.multiply(distance);
    }
    
    private static Scorer createFleeScorer() {
        Consideration lowHealth = Consideration.builder()
            .input(ctx -> (double) ctx.getEntity().getHealth() / ctx.getEntity().getMaxHealth())
            .curve(ResponseCurve.quadratic().invert()) // Low health = high score
            .build();
        
        Consideration enemyCount = Consideration.builder()
            .input(ctx -> (double) ctx.getBlackboard().get("enemy_count").orElse(0))
            .curve(ResponseCurve.linear())
            .range(0.0, 5.0)
            .build();
        
        return lowHealth.multiply(enemyCount).scale(1.2); // Boost flee priority
    }
    
    private static Scorer createCoverScorer() {
        // Peak at medium health (0.5)
        Consideration mediumHealth = Consideration.builder()
            .input(ctx -> (double) ctx.getEntity().getHealth() / ctx.getEntity().getMaxHealth())
            .curve(x -> 1.0 - Math.abs(x - 0.5) * 2.0) // Peak at 0.5
            .build();
        
        return mediumHealth.scale(0.8);
    }
}
```

## Dynamic Priority

For simpler cases, use `DynamicPrioritySelector`:

```java
DynamicPrioritySelector taskManager = new DynamicPrioritySelector()
    .addPrioritized(defendBase, ctx -> 
        ctx.getBlackboard().get("enemies_nearby").orElse(false) ? 1.0 : 0.1
    )
    .addPrioritized(repairBuildings, ctx -> 
        Math.min(1.0, ctx.getBlackboard().get("damaged_buildings").orElse(0) / 3.0)
    )
    .addPrioritized(gatherResources, ctx -> 
        1.0 - (ctx.getBlackboard().get("resource_level").orElse(100) / 100.0)
    );
```

## Tuning Tips

### 1. Start Simple
```java
// Begin with linear curves
Consideration simple = Consideration.builder()
    .input(inputFunction)
    .curve(ResponseCurve.linear())
    .build();
```

### 2. Visualize Curves
```java
for (double x = 0.0; x <= 1.0; x += 0.1) {
    System.out.println(x + " → " + curve.evaluate(x));
}
```

### 3. Log Scores
```java
ctx.getBlackboard().set("attack_score", attackScore.score(ctx));
ctx.getBlackboard().set("flee_score", fleeScore.score(ctx));
```

### 4. Adjust Re-evaluation
```java
// Fast-paced: 5-10 ticks
new UtilitySelector(5.0)

// Strategic: 20-40 ticks
new UtilitySelector(40.0)
```

## Performance

- Scorers run every re-evaluation interval
- Keep scorer logic simple and fast
- Cache expensive calculations in Blackboard
- Use appropriate intervals (don't re-evaluate every tick)

## When to Use

### ✅ Good For
- Complex decision-making
- Context-dependent behavior
- Smooth transitions
- Balancing multiple factors

### ❌ Not Ideal For
- Simple binary decisions (use ConditionalNode)
- Fixed sequences (use SequenceNode)
- Very performance-critical code

## Next Steps

- **[Scorers](Scorers)** - Deep dive into scoring
- **[Considerations](Considerations)** - Single-variable evaluation
- **[Response Curves](Response-Curves)** - All curve types
- **[Utility AI Examples](Utility-AI-Examples)** - Complete examples

---

**Questions?** Check the **[FAQ](FAQ)** or **[Utility AI Examples](Utility-AI-Examples)**!
