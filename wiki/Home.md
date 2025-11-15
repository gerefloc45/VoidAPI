#  switch (# Nemesis-API Wiki.Value) { "Nemesis-API" { "VoidAPI" } "Nemesis API" { "VoidAPI" } "nemesisapi" { "voidapi" } default { # Nemesis-API Wiki.Value } }  Wiki

Welcome to the ** switch (Welcome to the **Nemesis-API** documentation! This is a powerful, modular AI framework for Minecraft Fabric 1.21.1..Value) { "Nemesis-API" { "VoidAPI" } "Nemesis API" { "VoidAPI" } "nemesisapi" { "voidapi" } default { Welcome to the **Nemesis-API** documentation! This is a powerful, modular AI framework for Minecraft Fabric 1.21.1..Value } } ** documentation! This is a powerful, modular AI framework for Minecraft Fabric 1.21.1.

## 🚀 Quick Navigation

### Getting Started
- **[Installation](Installation)** - How to add  switch (- **[Installation](Installation)** - How to add Nemesis-API to your mod.Value) { "Nemesis-API" { "VoidAPI" } "Nemesis API" { "VoidAPI" } "nemesisapi" { "voidapi" } default { - **[Installation](Installation)** - How to add Nemesis-API to your mod.Value } }  to your mod
- **[Quick Start Guide](Quick-Start)** - Build your first AI in 5 minutes
- **[Project Setup](Project-Setup)** - Setting up your development environment

### Core Concepts
- **[Behavior Trees](Behavior-Trees)** - Understanding the foundation
- **[Blackboard System](Blackboard)** - Shared memory for AI
- **[Brain Controller](Brain-Controller)** - Managing entity AI
- **[Behavior Context](Behavior-Context)** - Execution context

### Behavior Nodes
- **[Basic Nodes](Basic-Nodes)** - Selector, Sequence, Action
- **[Advanced Nodes](Advanced-Nodes)** - Parallel, Repeat, Conditional, Cooldown, Inverter
- **[Utility AI Nodes](Utility-AI-Nodes)** - UtilitySelector, DynamicPriority

### Perception System
- **[Sensors Overview](Sensors-Overview)** - Detecting the environment
- **[Entity Sensor](Entity-Sensor)** - Track nearby entities
- **[Block Sensor](Block-Sensor)** - Detect specific blocks
- **[Sound Sensor](Sound-Sensor)** - Listen to sound events
- **[Perception Memory](Perception-Memory)** - Remember what you've seen

### Utility AI System
- **[Utility AI Overview](Utility-AI-Overview)** - Decision-making with scores
- **[Scorers](Scorers)** - Evaluating behaviors
- **[Considerations](Considerations)** - Single-variable evaluation
- **[Response Curves](Response-Curves)** - Shaping utility scores

### Examples & Tutorials
- **[Basic Examples](Basic-Examples)** - Simple AI patterns
- **[Combat AI Tutorial](Combat-AI-Tutorial)** - Smart fighting behavior
- **[Utility AI Examples](Utility-AI-Examples)** - Advanced decision-making
- **[Custom Behaviors](Custom-Behaviors)** - Creating your own nodes

### API Reference
- **[Core API](API-Core)** - Behavior, BehaviorNode, BehaviorTree
- **[Nodes API](API-Nodes)** - All available nodes
- **[Perception API](API-Perception)** - Sensors and memory
- **[Utility API](API-Utility)** - Utility AI components

### Advanced Topics
- **[Performance Optimization](Performance)** - Making AI run fast
- **[Debugging AI](Debugging)** - Tools and techniques
- **[Best Practices](Best-Practices)** - Writing maintainable AI
- **[Migration Guide](Migration-Guide)** - Upgrading between versions

### Community
- **[FAQ](FAQ)** - Frequently asked questions
- **[Troubleshooting](Troubleshooting)** - Common issues and solutions
- **[Contributing](Contributing)** - How to contribute to the project
- **[Changelog](Changelog)** - Version history

---

## 📊 Current Version

**v0.2.0-beta** - Utility AI System Complete

### What's Included
- ✅ Complete Behavior Tree system
- ✅ 11 behavior nodes (basic + advanced)
- ✅ 4 perception sensors
- ✅ Full Utility AI system
- ✅ 13+ response curves
- ✅ Comprehensive examples

### Quick Stats
- 📦 29 Java classes
- 🎯 11 behavior tree nodes
- 👁️ 6 perception classes
- 🧮 13+ response curves
- 📚 Complete documentation

---

## 🎯 What is  switch (## 🎯 What is Nemesis-API?.Value) { "Nemesis-API" { "VoidAPI" } "Nemesis API" { "VoidAPI" } "nemesisapi" { "voidapi" } default { ## 🎯 What is Nemesis-API?.Value } } ?

 switch (Nemesis-API is a **pure library mod** that provides advanced AI capabilities for Minecraft entities. It contains:.Value) { "Nemesis-API" { "VoidAPI" } "Nemesis API" { "VoidAPI" } "nemesisapi" { "voidapi" } default { Nemesis-API is a **pure library mod** that provides advanced AI capabilities for Minecraft entities. It contains:.Value } }  is a **pure library mod** that provides advanced AI capabilities for Minecraft entities. It contains:

- **No entities, items, or blocks** - Pure API only
- **Behavior Tree system** - Industry-standard AI architecture
- **Perception system** - Sensors for detecting the world
- **Utility AI** - Score-based decision making
- **Performance optimized** - Handles 100+ AI entities

### Perfect For
- ✅ Custom mob mods
- ✅ Boss fights with complex AI
- ✅ NPC companions
- ✅ Strategy game mechanics
- ✅ Any entity that needs smart behavior

---

## 💡 Why Use  switch (## 💡 Why Use Nemesis-API?.Value) { "Nemesis-API" { "VoidAPI" } "Nemesis API" { "VoidAPI" } "nemesisapi" { "voidapi" } default { ## 💡 Why Use Nemesis-API?.Value } } ?

### Traditional Minecraft AI
```java
// Vanilla Goal system - rigid and hard to extend
goalSelector.add(1, new MeleeAttackGoal(mob, 1.0, false));
goalSelector.add(2, new WanderAroundGoal(mob, 1.0));
```

###  switch (### Nemesis-API.Value) { "Nemesis-API" { "VoidAPI" } "Nemesis API" { "VoidAPI" } "nemesisapi" { "voidapi" } default { ### Nemesis-API.Value } } 
```java
// Flexible, composable, powerful
BehaviorTree brain = new BehaviorTree(
    new SelectorNode()
        .addChild(new SequenceNode()
            .addChild(findEnemyBehavior)
            .addChild(new CooldownNode(attackBehavior, 1.0f))
        )
        .addChild(patrolBehavior)
);

BrainController.getInstance().attachBrain(entity, brain);
```

### Key Advantages
- 🎯 **Composable** - Mix and match behaviors
- 🔄 **Reusable** - Share AI across different entities
- 🐛 **Debuggable** - Clear structure, easy to understand
- ⚡ **Performant** - Optimized for many entities
- 📈 **Scalable** - From simple to complex AI

---

## 🚀 Get Started Now!

1. **[Install  switch (1. **[Install Nemesis-API](Installation)** in your mod.Value) { "Nemesis-API" { "VoidAPI" } "Nemesis API" { "VoidAPI" } "nemesisapi" { "voidapi" } default { 1. **[Install Nemesis-API](Installation)** in your mod.Value } } ](Installation)** in your mod
2. **[Follow the Quick Start](Quick-Start)** guide
3. **[Check out Examples](Basic-Examples)** for inspiration
4. **[Join the Community](Contributing)** and share your creations!

---

## 📝 License

 switch (Nemesis-API is released under the [MIT License](https://github.com/yourusername/Nemesis-API/blob/main/LICENSE)..Value) { "Nemesis-API" { "VoidAPI" } "Nemesis API" { "VoidAPI" } "nemesisapi" { "voidapi" } default { Nemesis-API is released under the [MIT License](https://github.com/yourusername/Nemesis-API/blob/main/LICENSE)..Value } }  is released under the [MIT License](https://github.com/yourusername/ switch (Nemesis-API is released under the [MIT License](https://github.com/yourusername/Nemesis-API/blob/main/LICENSE)..Value) { "Nemesis-API" { "VoidAPI" } "Nemesis API" { "VoidAPI" } "nemesisapi" { "voidapi" } default { Nemesis-API is released under the [MIT License](https://github.com/yourusername/Nemesis-API/blob/main/LICENSE)..Value } } /blob/main/LICENSE).

---

**Need help?** Check the **[FAQ](FAQ)** or **[Troubleshooting](Troubleshooting)** pages!
