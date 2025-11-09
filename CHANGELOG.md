# Changelog

All notable changes to Nemesis-API will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Planned
- GOAP (Goal-Oriented Action Planning) system
- Debug visualization tools
- Advanced pathfinding integration
- Hierarchical behavior trees
- Behavior tree serialization

---

## [0.2.0-beta] - 2025-11-09

### Added - Utility AI System 🧮

#### Core Components
- **Scorer Interface** - Functional interface for evaluating behavior utility
  - `score(BehaviorContext)` method for calculating scores
  - Combinators: `add()`, `multiply()`, `scale()`, `clamp()`, `invert()`
  - Static factory: `Scorer.constant(double)`
  
- **Consideration Class** - Single-variable evaluation with normalization
  - Automatic input normalization to [0, 1] range
  - Builder pattern for easy configuration
  - Integration with ResponseCurve system
  - Custom input functions via lambda

- **ResponseCurve Interface** - Mathematical curves for shaping utility scores
  - **13+ predefined curves**: Linear, Quadratic, Cubic, Exponential, Logistic, Step, SmoothStep, Power, Sine, etc.
  - **Curve combinators**: `invert()`, `scale()`, `offset()`, `clamp()`, `then()`
  - Custom curve support via lambda
  - Configurable parameters (slope, steepness, power, etc.)

- **UtilitySelector Node** - Behavior tree node that selects child with highest score
  - Configurable re-evaluation interval
  - Dynamic behavior switching at runtime
  - Score tracking for debugging
  - Support for constant scores

- **DynamicPrioritySelector Node** - Selector that reorders children by priority
  - Dynamic priority calculation via Scorer
  - Automatic child reordering
  - Priority tracking and debugging support
  - Fixed and dynamic priority support

#### Documentation
- Complete Utility AI examples (Combat, Resource Gathering, Task Management)
- Response curve usage guide
- Best practices and performance tips
- Tuning guide for considerations and curves

#### Examples
- Smart Combat AI with attack/flee/cover decisions
- Resource gathering AI with inventory tracking
- Task manager AI with dynamic priorities
- Response curve demonstrations

### Changed
- Updated project to v0.2.0-beta
- Enhanced documentation structure
- Improved code organization in utility package

### Statistics
- **29 Java classes** total (+5 from v0.1.0)
- **11 behavior tree nodes** (+2 utility nodes)
- **13+ response curves** predefined
- **50% progress** towards v1.0.0 stable

---

## [0.1.0-beta] - 2025-11-09

### Added - Core Framework & Perception System 🎯

#### Core Framework
- **Behavior Interface** - Base interface for all behaviors
  - `execute()`, `onStart()`, `onEnd()` lifecycle methods
  - Status enum: SUCCESS, FAILURE, RUNNING
  
- **BehaviorNode Abstract Class** - Base class for tree nodes
  - Lifecycle management
  - Child behavior support
  
- **BehaviorTree Class** - Root container for behavior trees
  - Tree execution and ticking
  - Root node management
  
- **BehaviorContext Class** - Execution context
  - Entity reference
  - Blackboard access
  - World access
  
- **Blackboard Class** - Shared memory system
  - Type-safe key-value storage
  - Optional-based retrieval
  - Memory management
  
- **BrainController Singleton** - Centralized AI management
  - Brain attachment/detachment
  - Entity-to-brain mapping
  - Lifecycle management
  
- **BrainTicker Class** - Automatic AI ticking
  - Entity registration system
  - Tick event handling
  - Performance optimization

#### Basic Nodes
- **SelectorNode** - OR logic, tries children until one succeeds
- **SequenceNode** - AND logic, runs children until one fails
- **ActionNode** - Leaf node for custom actions

#### Advanced Nodes
- **ParallelNode** - Runs multiple children simultaneously
  - REQUIRE_ONE and REQUIRE_ALL policies
  - Individual child status tracking
  - Configurable success/failure conditions
  
- **RepeatNode** - Repeats child behavior N times
  - Finite and infinite loop support
  - Iteration counting
  
- **ConditionalNode** - Executes child only if condition is true
  - Predicate-based conditions
  - Blackboard integration
  
- **CooldownNode** - Adds cooldown timer to child behavior
  - Configurable cooldown duration
  - Tick-based timing
  
- **InverterNode** - Inverts child's success/failure status
  - SUCCESS ↔ FAILURE inversion
  - RUNNING passes through

#### Perception System
- **Sensor Interface** - Base interface for all sensors
  - `update()`, `reset()` methods
  - Configurable update frequency
  - Range and active state
  
- **SensorManager Class** - Manages multiple sensors per entity
  - Sensor registration and lifecycle
  - Frequency-based updates
  - Tick counting and optimization
  
- **EntitySensor** - Detects nearby entities
  - Class-based filtering
  - Line-of-sight checking
  - Custom predicate filtering
  - Nearest entity tracking
  - Builder pattern configuration
  
- **BlockSensor** - Detects specific blocks in environment
  - Multiple scanning patterns: FULL_SPHERE, HORIZONTAL_PLANE, CARDINAL_DIRECTIONS, FORWARD_CONE
  - Block type filtering with tags
  - Nearest block tracking
  - Configurable scan resolution
  - Builder pattern configuration
  
- **SoundSensor** - Detects sound events
  - Event-based detection
  - Temporal memory (remembers recent sounds)
  - Sound type filtering
  - Nearest sound tracking
  - Configurable memory duration
  - Builder pattern configuration
  
- **PerceptionMemory** - Remembers detected entities/objects
  - Time-based memory decay
  - Last known position tracking
  - Threat level tracking
  - Memory cleanup

#### Documentation
- Comprehensive README with examples
- AI-ROADMAP with development phases
- Code documentation and JavaDocs
- Example implementations

#### Examples
- Basic behavior tree patterns
- Sensor usage examples
- Combat AI patterns
- Perception system integration

### Statistics
- **24 Java classes** implemented
- **9 behavior tree nodes** (3 basic + 5 advanced + 1 decorator)
- **6 perception classes** (4 sensors + manager + memory)
- **100% Phase 1** completion

---

## Project Information

### Version Naming
- **Major.Minor.Patch-Stage**
- Example: `0.2.0-beta`
- Stages: `alpha`, `beta`, `rc` (release candidate), `stable`

### Compatibility
- **Minecraft**: 1.21.1
- **Fabric Loader**: 0.16.9+
- **Fabric API**: 0.106.0+
- **Java**: 21

### Links
- **Repository**: https://github.com/yourusername/Nemesis-API
- **Issues**: https://github.com/yourusername/Nemesis-API/issues
- **Wiki**: https://github.com/yourusername/Nemesis-API/wiki

### License
MIT License - See LICENSE file for details

---

## Legend

### Change Types
- **Added** - New features
- **Changed** - Changes to existing functionality
- **Deprecated** - Soon-to-be removed features
- **Removed** - Removed features
- **Fixed** - Bug fixes
- **Security** - Security fixes

### Emojis
- 🎯 Core Framework
- 🧮 Utility AI
- 👁️ Perception System
- 🎮 GOAP System
- 🔧 Debug Tools
- 🗺️ Pathfinding
- 📚 Documentation
- 🐛 Bug Fix
- ⚡ Performance
- 🔒 Security

---

**Last Updated**: 2025-11-09  
**Current Version**: v0.2.0-beta
