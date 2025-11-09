# Nemesis-API Framework - Roadmap & Future Implementations

## 📊 Status Attuale
- ✅ **v0.1.0-beta** - Core framework completato e funzionante
- ✅ Behavior Tree base (Selector, Sequence, Action)
- ✅ Blackboard memory system
- ✅ Brain controller & auto-ticker
- ✅ Async utilities
- ✅ Entity utilities base
- ✅ 5 Nodi avanzati (Parallel, Inverter, Repeat, Conditional, Cooldown)
- ✅ Sistema percezione completo (Sensors, Memory, Manager)

---

## 🚀 Implementazioni Pianificate

### Phase 1: Nodi Avanzati + Sensori ✅ COMPLETATO
**Priorità**: ALTA  
**Completato**: 2025-11-08  
**Versione**: v0.1.0-beta

#### Nodi Behavior Tree Avanzati
- [ ] **ParallelNode** - Esegue più behavior contemporaneamente
  - Modalità: RequireAll, RequireOne
  - Gestione fallimenti paralleli
  
- [ ] **DecoratorNode** - Base per decoratori
  - Modifica comportamento child
  - Lifecycle management
  
- [ ] **InverterNode** - Inverte SUCCESS/FAILURE
  - Utile per condizioni negative
  
- [ ] **RepeatNode** - Ripete behavior N volte o infinitamente
  - Repeat until failure
  - Repeat N times
  - Infinite repeat con break condition
  
- [ ] **ConditionalNode** - Esegue solo se condizione vera
  - Lambda condition support
  - Blackboard condition checks
  
- [ ] **CooldownNode** - Aggiunge cooldown tra esecuzioni
  - Tempo configurabile
  - Blackboard-based tracking

#### Sistema di Sensori (Perception)
- [ ] **Sensor API** - Interfaccia base per sensori
  - Update frequency configurabile
  - Range detection
  
- [ ] **EntitySensor** - Rileva entità vicine
  - Filtri per tipo entità
  - Line of sight check
  - Memory persistence
  
- [ ] **BlockSensor** - Rileva blocchi specifici
  - Pattern matching
  - Area scanning
  
- [ ] **SoundSensor** - Rileva suoni/eventi
  - Event-based detection
  
- [ ] **MemorySystem** - Ricorda entità viste
  - Decay time configurabile
  - Last known position
  - Threat level tracking

#### Esempi Pratici
- [ ] GuardAI con sensori
- [ ] PatrolAI con waypoints
- [ ] HunterAI con tracking

---

### Phase 2: Utility AI System
**Priorità**: MEDIA  
**Tempo Stimato**: 40-50 minuti  
**Versione Target**: v0.2.0

#### Core Utility System
- [ ] **Scorer Interface** - Sistema di punteggio per behaviors
- [ ] **Consideration** - Valutazione singola variabile
- [ ] **ResponseCurve** - Curve di valutazione
  - Linear
  - Quadratic
  - Exponential
  - Custom curves
  
- [ ] **UtilitySelector** - Seleziona behavior con score più alto
- [ ] **DynamicPriority** - Priorità che cambiano runtime

#### Esempi
- [ ] Combat AI con utility scoring
- [ ] Resource gathering AI
- [ ] Social interaction AI

---

### Phase 3: Goal-Oriented Action Planning (GOAP)
**Priorità**: MEDIA  
**Tempo Stimato**: 60-90 minuti  
**Versione Target**: v0.3.0

#### GOAP Core
- [ ] **Goal System** - Definizione obiettivi
  - Priority-based goals
  - Goal conditions
  
- [ ] **WorldState** - Rappresentazione stato mondo
  - Key-value state
  - State comparison
  
- [ ] **Action** - Azioni atomiche
  - Preconditions
  - Effects
  - Cost
  
- [ ] **Planner** - A* based planning
  - Path finding tra stati
  - Cost optimization
  - Plan caching

#### Esempi
- [ ] Survival AI (trova cibo, rifugio, difesa)
- [ ] Builder AI (raccoglie risorse, costruisce)
- [ ] Trader AI (cerca oggetti, commercia)

---

### Phase 4: Debug & Visualization Tools
**Priorità**: ALTA (per sviluppo)  
**Tempo Stimato**: 45-60 minuti  
**Versione Target**: v0.4.0

#### Debug Tools
- [ ] **TreeVisualizer** - Visualizza behavior tree in esecuzione
  - Real-time node status
  - Execution path highlighting
  - Web-based UI o in-game overlay
  
- [ ] **PerformanceProfiler** - Misura performance
  - Tempo esecuzione per nodo
  - Memory usage tracking
  - Bottleneck detection
  
- [ ] **BlackboardInspector** - Ispeziona blackboard runtime
  - Live value monitoring
  - History tracking
  - Value editing (debug mode)
  
- [ ] **AIDebugCommands** - Comandi in-game
  - `/nemesisapi debug <entity>` - Mostra AI info
  - `/nemesisapi tree <entity>` - Visualizza tree
  - `/nemesisapi blackboard <entity>` - Mostra memoria
  - `/nemesisapi profile <entity>` - Performance stats

#### Logging System
- [ ] Structured logging per AI events
- [ ] Log levels configurabili
- [ ] File output per analisi

---

### Phase 5: Pathfinding Integration
**Priorità**: MEDIA  
**Tempo Stimato**: 50-70 minuti  
**Versione Target**: v0.5.0

#### Pathfinding Core
- [ ] **PathfinderAPI** - Integrazione con Minecraft pathfinding
- [ ] **A* Implementation** - Custom A* per casi speciali
- [ ] **DynamicObstacles** - Evita ostacoli dinamici
- [ ] **MovementCapabilities** - Jump, swim, climb support
- [ ] **PathSmoothing** - Percorsi più naturali
- [ ] **PathCache** - Cache percorsi calcolati

#### Advanced Features
- [ ] **FormationMovement** - Movimento in formazione
- [ ] **FlockingBehavior** - Comportamento gregge
- [ ] **CoverSystem** - Trova coperture in combattimento

---

### Phase 6: Advanced AI Patterns
**Priorità**: BASSA  
**Tempo Stimato**: Variabile  
**Versione Target**: v1.0.0 (Stable)

#### Patterns Avanzati
- [ ] **State Machine Integration** - FSM + Behavior Trees
- [ ] **Hierarchical Task Network (HTN)** - Planning gerarchico
- [ ] **Behavior Fusion** - Combina multiple AI
- [ ] **Learning System** - AI che impara (basic ML)
- [ ] **Emotion System** - Stati emotivi che influenzano AI
- [ ] **Social AI** - Interazioni tra entità
- [ ] **Squad AI** - Coordinazione gruppi

---

### Phase 7: Performance & Optimization
**Priorità**: ALTA (long-term)  
**Tempo Stimato**: Continuo  
**Versione Target**: Ongoing

#### Ottimizzazioni
- [ ] **Behavior Pooling** - Riuso oggetti behavior
- [ ] **Lazy Evaluation** - Valuta solo quando necessario
- [ ] **LOD System** - Level of Detail per AI distanti
- [ ] **Multithreading** - Parallel AI updates
- [ ] **Spatial Partitioning** - Ottimizza query spaziali
- [ ] **Incremental Planning** - Planning distribuito su più tick

---

### Phase 8: Integration & Ecosystem
**Priorità**: MEDIA  
**Tempo Stimato**: Variabile  
**Versione Target**: v1.x (Post-Stable)

#### Integrazioni
- [ ] **Datapack Support** - Definisci AI via datapack
- [ ] **JSON Configuration** - AI configurabili via JSON
- [ ] **Mod Compatibility** - Integration con altri mod popolari
- [ ] **API Extensions** - Plugin system per estendere framework
- [ ] **Preset Library** - Libreria AI pre-fatte riusabili

---

## 📈 Metriche di Successo

### Performance Targets
- ⚡ < 0.5ms per brain tick (media)
- 🧠 Supporto 100+ entità con AI simultanee
- 💾 < 1KB memoria per brain instance
- 🔄 60 TPS stabili con 50+ AI attive

### Developer Experience
- 📚 100% API documentation coverage
- 🎯 < 10 linee codice per AI semplice
- 🛠️ Debug tools completi
- 📖 Esempi per ogni feature

### Ecosystem
- 🔌 5+ mod che usano Nemesis-API
- 👥 Community contributions
- 📦 Preset library con 20+ AI templates

---

## 🗓️ Timeline Stimata

- **v1.1.0** (Nodi + Sensori): ~1 settimana
- **v1.2.0** (Utility AI): ~1 settimana
- **v1.3.0** (GOAP): ~2 settimane
- **v1.4.0** (Debug Tools): ~1 settimana
- **v1.5.0** (Pathfinding): ~2 settimane
- **v2.0.0** (Advanced): ~1 mese

**Total**: ~2-3 mesi per feature complete

---

## 💡 Idee Future (Brainstorming)

### Possibili Aggiunte
- Neural Network integration per ML
- Genetic Algorithm per evoluzione AI
- Swarm Intelligence patterns
- Procedural behavior generation
- Voice command integration
- VR/AR debug visualization
- Cloud-based AI training
- Cross-server AI synchronization

### Community Requests
- [ ] Placeholder per richieste community
- [ ] Voting system per priorità features

---

## 📝 Note di Implementazione

### Principi Guida
1. **Keep it Simple** - API deve rimanere semplice
2. **Performance First** - Ottimizza sempre
3. **Modular Design** - Ogni feature è opzionale
4. **Backward Compatible** - Non rompere API esistenti
5. **Well Documented** - Javadoc + esempi sempre

### Breaking Changes Policy
- Major version (2.0, 3.0): Breaking changes permessi
- Minor version (1.1, 1.2): Solo additive changes
- Patch version (1.0.1): Solo bugfix

---

## 🤝 Contributi

Questo è un progetto in evoluzione. Idee e suggerimenti sono benvenuti!

**Ultima Modifica**: 2025-11-08  
**Versione Corrente**: v1.0.0  
**Prossima Release**: v1.1.0 (Nodi Avanzati + Sensori)
