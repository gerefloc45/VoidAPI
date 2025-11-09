# Nemesis-API Framework

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE) [![Minecraft](https://img.shields.io/badge/Minecraft-1.21.1-62b47a)](#) [![Fabric](https://img.shields.io/badge/Fabric-API%20Ready-blueviolet)](#) [![Version](https://img.shields.io/badge/Version-0.1.0--beta-orange)](#)

**Nemesis-API** is a powerful, modular AI framework for Minecraft Fabric 1.21.1. Build advanced entity AI using behavior trees, sensors, and perception systems - no gameplay content included, just pure API.

> ⚠️ **Beta Version** - This project is in active development (v0.1.0-beta). APIs may change between versions.  
> 🎯 **Pure Library Mod** - Nemesis-API contains no entities, items, or gameplay content. It's designed to be used as a dependency for your custom mods.

## 🚀 Quick Links

- **[📖 Getting Started Guide](GETTING-STARTED.md)** - Learn the basics in 5 minutes ⭐
- **[📚 Complete Examples](EXAMPLES.md)** - Practical, copy-paste ready examples
- **[🗺️ Roadmap](AI-ROADMAP.md)** - Future features and development plan
- **[📘 Full Documentation](README-NEMESIS-API.md)** - Complete API reference
- **[📑 Documentation Index](DOCUMENTATION-INDEX.md)** - Find anything quickly

---

## ✨ Features

### Core System (v0.1.0-beta)
- 🌳 **Behavior Tree System** - Selector, Sequence, and Action nodes
- 💾 **Blackboard Memory** - Per-entity shared memory
- 🧠 **Brain Controller** - Centralized AI management
- ⚡ **Async Utilities** - Non-blocking operations
- 🔄 **Auto-Ticking** - Automatic updates via Fabric events

### Advanced Features (v0.1.0-beta) 🆕
- 🔀 **ParallelNode** - Execute multiple behaviors simultaneously
- 🔄 **RepeatNode** - Loop behaviors with limits
- ⏱️ **CooldownNode** - Add cooldowns between executions
- ❓ **ConditionalNode** - Execute based on conditions
- 🔃 **InverterNode** - Invert behavior results

### Perception System (v0.1.0-beta) 🆕
- 👁️ **Sensor API** - Detect entities and environment
- 🎯 **EntitySensor** - Track nearby entities with filters
- 🧠 **PerceptionMemory** - Remember entities after they leave range
- 📡 **SensorManager** - Manage multiple sensors efficiently

---

## 🎯 Quick Example

```java
// Create a simple guard AI
BehaviorTree brain = new BehaviorTree(
    new SelectorNode()
        .addChild(new SequenceNode()
            .addChild(findEnemyBehavior)
            .addChild(new CooldownNode(attackBehavior, 1.0f))
        )
        .addChild(patrolBehavior)
);

// Attach to entity
BrainController.getInstance().attachBrain(entity, brain);
BrainTicker.registerEntity(entity);
```

**That's it!** Your entity now has advanced AI. See [Getting Started](GETTING-STARTED.md) for more.

---

## 📦 Installation

```gradle
dependencies {
    modImplementation "com.gerefloc45:nemesis-api:0.1.0"
    include "com.gerefloc45:nemesis-api:0.1.0"
}
```

---

## Project Structure

```
src/main/java/com/gerefloc45/nemesisapi/
├─ NemesisAPIMod.java         // Main mod initialization
├─ api/                       // Public API interfaces and classes
│  ├─ Behavior.java           // Base behavior interface
│  ├─ BehaviorTree.java       // Behavior tree system
│  ├─ Blackboard.java         // Shared memory system
│  └─ nodes/                  // Behavior tree nodes
├─ core/                      // Core framework components
│  ├─ BrainController.java    // Entity AI management
│  └─ BrainTicker.java        // AI update system
└─ util/                      // Utility classes
```

In addition the repository ships with:

* `build.gradle` – Fabric 1.21.1 build script (applies to any Gradle-based mod workspace).
* `LICENSE` – MIT license.

---

## Registries & providers

Nemesis-API provides a centralized brain controller through the `BrainController` class.
It stores suppliers (factories) for boss AI, mob AI, target selectors and animation managers. Using
suppliers instead of concrete instances ensures that each entity obtains a fresh controller and that
state is not leaked across instantiations.

### Registering AI controllers

```java
public class MyMod implements ModInitializer {
    @Override
    public void onInitialize() {
        NemesisAIRegistry.registerBossAI("mymod:abyss_watcher", () -> new AbyssWatcherAI());
        NemesisAIRegistry.registerMobAI("mymod:abyss_stalker", () -> new AbyssStalkerAI());
    }
}
```

Whenever an entity spawns you can request a new controller:

```java
BossAI bossAI = NemesisAIRegistry.createBossAI("mymod:abyss_watcher").orElseThrow();
AnimationManager manager = NemesisAIRegistry.createAnimationManager("mymod:default_manager")
        .orElseThrow();
```

### Target selectors

`TargetSelector` defines a single method, `selectTarget`, that receives an abstract `TargetingContext`.
Modders can implement simple lambdas or full-fledged evaluators that inspect distance caches, line of
sight attributes and threat tables stored in `TargetingContext#attributes()`.

```java
TargetSelector seeker = context -> context.candidates().stream()
        .filter(candidate -> context.getAttribute("distance:" + candidate, Double.class)
                .map(distance -> distance < 12.0)
                .orElse(false))
        .findFirst();

NemesisAIRegistry.registerTargetSelector("mymod:close_range", () -> seeker);
```

### Animation managers

The registry also exposes `registerAnimationManager`. A manager is responsible for converting
abstract descriptors into actual bone transforms. They can wrap your own IK solver, make network
calls, or integrate third-party systems.

```java
public final class GeckoBridgeManager implements AnimationManager {
    private final GeckoBridgeRuntime runtime = new GeckoBridgeRuntime();

    @Override
    public void enqueueDescriptor(AnimationDescriptor descriptor) {
        runtime.queue(descriptor.type(), descriptor.channel(), descriptor.parameters());
    }

    @Override
    public void enqueueBossAction(BossAction action) {
        BossAction.ActionPlan plan = action.plan(new BossAction.RuntimeContext(this, runtime.getEntity(),
                Optional.ofNullable(runtime.getEntity().getTarget()), runtime.getBlackboard(),
                runtime.getGameTime()));
        enqueueCombo(plan.channel(), plan.sequence(), plan.loop());
    }

    // ... implement other methods as suits your runtime ...
}

NemesisAIRegistry.registerAnimationManager("mymod:gecko_bridge", GeckoBridgeManager::new);
```

---

## AI contracts

### `BossAI`

Boss controllers drive high-level combat logic.

* `tickAI(FrameContext)` – called every server tick. It exposes game time, the host entity,
  a shared blackboard map and the currently selected target.
* `getTargetSelector()` – provides the targeting strategy for the boss.
* `requestBossAction(AnimationManager, BossAction)` / `requestAnimation(AnimationManager, AnimationDescriptor)` –
  helper methods for scheduling runtime animations.

`FrameContext` also exposes `submitAnimation` and `submitAction` helpers so you can queue descriptors
from within the tick loop without referencing the `AnimationManager` directly.

### `MobAI`

Mob controllers are similar to boss controllers but intentionally slimmer. They can still request
`BossAction` sequences, enabling elites or mini-bosses to share complex combos.

### `BossAction`

An action is an orchestrator that translates combat intent into a sequence of descriptors. Override
`plan(RuntimeContext)` and return an `ActionPlan` with ordered descriptors and metadata for the
animation manager. Typical use cases include:

* multi-phase combos (e.g., left slash → spin → slam)
* grapples or grabs (e.g., extend arm, attach target, swing)
* finishers with cinematic timing, damage windows or camera cues stored in the metadata map

```java
public final class UppercutAction extends BossAction {
    public UppercutAction() {
        super("mymod:uppercut", 10, false);
    }

    @Override
    public ActionPlan plan(RuntimeContext context) {
        AnimationDescriptor windup = AnimationDescriptor.builder("uppercut_windup")
                .durationSeconds(0.8f)
                .intensity(0.4f)
                .channel("body")
                .addTag("melee")
                .parameter("weight_shift", 0.7f)
                .build();

        AnimationDescriptor strike = AnimationDescriptor.builder("uppercut_strike")
                .durationSeconds(0.45f)
                .intensity(1.4f)
                .comboStep(1)
                .parameter("launch_force", 1.0f)
                .build();

        return new ActionPlan(List.of(windup, strike), "body", false,
                Map.of("damage_window", List.of(0.45f, 0.6f)));
    }
}
```

### `AnimationDescriptor`

Descriptors are immutable data packets describing a single motion. They define the channel,
intensity, yaw/pitch offsets, blend modes, combo step information and arbitrary parameters/tags. Use
the fluent builder to provide only the data you need.

---

## Procedural animation workflow

1. **AI tick** – your `BossAI`/`MobAI` runs, reading/writing to the blackboard and selecting targets.
2. **Intent description** – the AI calls `requestAnimation` or `requestBossAction` with an abstract
   description (`AnimationDescriptor` or `BossAction`).
3. **Animation synthesis** – the chosen `AnimationManager` inspects descriptors, generates bones,
   handles IK, attaches victims, performs blending/physics, and streams the resulting pose to clients.
4. **Playback feedback** – managers expose `PlaybackState` which your AI can poll for progress,
   allowing state machines to branch when combos finish or to trigger finishers when health is low.

This pipeline ensures that modders focus on logic while animation specialists extend the manager.

---

## Modder best practices

* Treat the `blackboard` maps in `FrameContext` and `RuntimeContext` as your shared memory. Store
  cooldowns, last-used combo steps, or weight multipliers there.
* Keep identifiers namespaced (`my_mod:attack_combo_a`) to avoid collisions with other mods.
* When creating descriptors, prefer semantic tags (`"grab"`, `"slam"`, `"launch"`) over brittle
  numeric flags. Animation managers can use these to swap entire sub-routines at runtime.
* Do not assume a concrete animation engine. Always talk to the generic `AnimationManager` API.
* Use `AnimationManager#enqueueCombo` for quick, parameterised sequences such as footwork loops.
* Override `AnimationManager#setChannelWeight` when you need custom blend curves (e.g. easing in a
  shield block pose while keeping locomotion running).

---

## FAQ

**Q: Where are the actual mobs or models?**  
NemesisAI intentionally ships zero assets. It is meant to sit alongside your existing entity code or
serve as a shared dependency for multiple gameplay mods.

**Q: Do I need to use a particular animation library?**  
No. Implement `AnimationManager` however you like. You can bridge to GeckoLib, use Fabric's pose
stack directly, or implement your own IK solver.

**Q: Can I preview animations in a tool?**  
Yes, but the preview must consume `AnimationDescriptor` metadata and generate the animation on the
fly. NemesisAI does not prescribe a GUI – it just exposes the data contracts.

**Q: How do grabs or finishers work without baked animations?**  
Describe the intent using `BossAction` or multiple `AnimationDescriptor`s. Include metadata such as
`"victim_anchor"` or `"camera_focus"`. Your `AnimationManager` reads these parameters and positions
entities accordingly at runtime.

**Q: Can I override animations per difficulty?**  
Absolutely. Store modifiers in the AI blackboard (e.g. `blackboard.put("difficulty_multiplier", 1.5f)`)
then tweak descriptor intensities based on those values before submission.

---

## Contributing

Issues and pull requests are welcome! Please document new registry entries and describe how your
animation managers interpret custom metadata so other mods can remain compatible.

---

## License

Nemesis-API is released under the [MIT License](LICENSE).
