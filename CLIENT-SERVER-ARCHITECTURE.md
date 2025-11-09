# VoidAPI - Client/Server Architecture

## 🎯 Overview

VoidAPI v0.2.0 is designed to work seamlessly on **both client and server** sides of Minecraft.

---

## 🏗️ Architecture

### Environment Configuration

```json
"environment": "*"
```

This means VoidAPI loads on:
- ✅ **Dedicated Servers** (Server-only)
- ✅ **Single Player** (Client + Server)
- ✅ **Multiplayer Clients** (Client-side)

---

## 📦 Entrypoints

### Main Entrypoint (Both Sides)
```java
VoidAPIMod implements ModInitializer
```

Initializes on **both client and server**:
- BrainTicker system
- Animation system
- Core framework

### Client Entrypoint (Client Only)
```java
VoidAPIClient implements ClientModInitializer
```

Initializes **only on the client side**:
- Client-specific features (future)
- Debug visualization (future)
- Client-side utilities

---

## 🔄 Component Distribution

### Server-Side Components
- ✅ **Behavior Trees** - AI logic execution
- ✅ **Blackboard** - Entity memory system
- ✅ **Pathfinding** - Navigation calculations
- ✅ **Perception** - Sensor system
- ✅ **Utility AI** - Decision making
- ✅ **Animation Controller** - Animation state management

### Client-Side Components
- ✅ **Animation Rendering** - Visual feedback
- ✅ **Debug Tools** - Visualization (optional)
- ✅ **Performance Monitoring** - Client metrics

### Shared Components
- ✅ **All of the above** - Work on both sides

---

## 🎮 Usage Scenarios

### Scenario 1: Dedicated Server
```
Server: Full VoidAPI framework running
Client: No VoidAPI (unless another mod requires it)
Result: AI works perfectly on server
```

### Scenario 2: Single Player
```
Client: Full VoidAPI framework running
Server: Full VoidAPI framework running (integrated)
Result: AI works on both sides, seamlessly
```

### Scenario 3: Multiplayer
```
Server: Full VoidAPI framework running
Client: Full VoidAPI framework running
Result: Server controls AI, client receives updates
```

---

## 🔐 Thread Safety

All VoidAPI components are **thread-safe** for:
- Server tick thread
- Client tick thread
- Async operations (via AsyncHelper)

---

## 📊 Performance Impact

### Server-Side
- Minimal overhead per entity
- Scales well with entity count
- Async operations supported

### Client-Side
- No impact if not used
- Optional debug tools
- Efficient rendering integration

---

## 🛠️ Development Guidelines

### For Mod Developers

When using VoidAPI in your mod:

```java
// Works on both sides automatically
BehaviorTree tree = new BehaviorTree(rootBehavior);

// Works on both sides
Blackboard blackboard = new Blackboard();

// Works on both sides
EntitySensor sensor = new EntitySensor.Builder()
    .range(16.0)
    .build();
```

### For Server-Only Features

If you need server-only logic:

```java
if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) {
    // Server-only code
}
```

### For Client-Only Features

If you need client-only logic:

```java
if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
    // Client-only code
}
```

---

## 🔗 Dependencies

### Required
- Fabric Loader 0.15.0+
- Fabric API (any version)
- Minecraft 1.21.1
- Java 17+

### Optional
- GeckoLib (for advanced animations)

---

## 📝 Notes

1. **No Gameplay Content**: VoidAPI is a library mod with no gameplay features
2. **Library Mod**: Other mods depend on VoidAPI to use its features
3. **Fully Modular**: Each component can be used independently
4. **Side-Agnostic**: Most components don't care about client/server distinction

---

## 🚀 Future Enhancements

Planned client-side features for v0.3.0+:
- Visual behavior tree debugging
- Real-time performance graphs
- Entity AI visualization
- Debug overlay

---

## 📞 Support

For questions about client/server architecture:
- Check the [API Documentation](README-VOIDAPI.md)
- Review [Examples](EXAMPLES.md)
- Open an issue on [GitHub](https://github.com/gerefloc45/VoidAPI/issues)
