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

## ✅ Implementazioni Completate

### v0.1.0-beta (Novembre 2025)

#### Core Framework
- ✅ **BehaviorTree** - Sistema completo di behavior tree
- ✅ **BehaviorNode** - Base astratta per tutti i nodi
- ✅ **Blackboard** - Sistema di memoria condivisa thread-safe
- ✅ **BrainController** - Gestione centralizzata AI
- ✅ **BrainTicker** - Sistema di aggiornamento automatico
- ✅ **AsyncHelper** - Utilità per operazioni asincrone
- ✅ **EntityUtil** - Utilità per entità

#### Nodi Base
- ✅ **SelectorNode** - Esegue children fino al primo SUCCESS
- ✅ **SequenceNode** - Esegue children fino al primo FAILURE
- ✅ **ActionNode** - Nodo foglia per azioni concrete

#### Nodi Avanzati (5 implementati)
- ✅ **ParallelNode** - Esecuzione parallela con modalità RequireAll/RequireOne
- ✅ **InverterNode** - Inversione risultato SUCCESS↔FAILURE
- ✅ **RepeatNode** - Ripetizione con limite o infinita
- ✅ **ConditionalNode** - Esecuzione condizionale basata su Predicate
- ✅ **CooldownNode** - Gestione cooldown tra esecuzioni

#### Sistema Percezione Completo
- ✅ **Sensor** - Interfaccia base per sensori
- ✅ **EntitySensor** - Rilevamento entità con filtri avanzati
- ✅ **PerceptionMemory** - Sistema memoria per entità rilevate
- ✅ **SensorManager** - Gestione multipli sensori per entità
- ✅ **BlockSensor** - Rilevamento blocchi con pattern scanning
- ✅ **SoundSensor** - Rilevamento eventi sonori con memoria temporale

#### Statistiche v0.1.0-beta
- 📦 **24 classi Java** implementate
- 🎯 **9 nodi behavior tree** disponibili
- 👁️ **6 classi perception** complete (4 sensori)
- 📚 **Documentazione completa** con esempi
- ⚡ **Performance ottimizzate** per 100+ entità

---

## ✅ v0.2.0-beta Completata

### Utility AI System (Novembre 2025)

#### Core Components
- ✅ **Scorer** - Interface funzionale per valutazione behaviors
- ✅ **Consideration** - Valutatore singola variabile con normalizzazione
- ✅ **ResponseCurve** - 13+ curve predefinite + custom
- ✅ **UtilitySelector** - Nodo che sceglie behavior migliore
- ✅ **DynamicPrioritySelector** - Priorità dinamiche runtime

#### Features Avanzate
- ✅ Combinatori per Scorer (add, multiply, scale, etc.)
- ✅ Curve combinabili e componibili
- ✅ Builder pattern per configurazione facile
- ✅ Debug support con score tracking
- ✅ Re-evaluation configurabile

#### Statistiche v0.2.0-beta
- 📦 **29 classi Java** totali (+5)
- 🎯 **11 nodi behavior tree** disponibili (+2)
- 🧮 **13+ response curves** predefinite
- 📚 **3 esempi completi** (Combat, Resource, Tasks)
- ⚡ **Ottimizzazioni** per decision-making efficiente

---

## 🚀 Implementazioni Pianificate

### Phase 1: Nodi Avanzati + Sensori ✅ COMPLETATO AL 100%
**Priorità**: ALTA  
**Completato**: 2025-11-09  
**Versione**: v0.1.0-beta

**🎉 Phase 1 Completata con Successo!**
- Tutti i 5 nodi avanzati implementati e testati
- Sistema percezione completo con 4 sensori differenti
- Tutti gli esempi e documentazione completata
- Nessun punto rimasto in sospeso

#### Nodi Behavior Tree Avanzati
- [x] **ParallelNode** - Esegue più behavior contemporaneamente
  - Modalità: RequireAll, RequireOne
  - Gestione fallimenti paralleli
  
- [x] **DecoratorNode** - Base per decoratori *(implementato via nodi specifici)*
  - Modifica comportamento child
  - Lifecycle management
  
- [x] **InverterNode** - Inverte SUCCESS/FAILURE
  - Utile per condizioni negative
  
- [x] **RepeatNode** - Ripete behavior N volte o infinitamente
  - Repeat until failure
  - Repeat N times
  - Infinite repeat con break condition
  
- [x] **ConditionalNode** - Esegue solo se condizione vera
  - Lambda condition support
  - Blackboard condition checks
  
- [x] **CooldownNode** - Aggiunge cooldown tra esecuzioni
  - Tempo configurabile
  - Blackboard-based tracking

#### Sistema di Sensori (Perception)
- [x] **Sensor API** - Interfaccia base per sensori
  - Update frequency configurabile
  - Range detection
  
- [x] **EntitySensor** - Rileva entità vicine
  - Filtri per tipo entità
  - Line of sight check
  - Memory persistence
  
- [x] **BlockSensor** - Rileva blocchi specifici
  - Pattern matching (FULL_SPHERE, HORIZONTAL_PLANE, CARDINAL_DIRECTIONS, FORWARD_CONE)
  - Area scanning ottimizzata
  - Builder pattern per configurazione
  
- [x] **SoundSensor** - Rileva suoni/eventi
  - Event-based detection
  - Memoria temporale configurabile
  - Filtraggio per tipo suono
  
- [x] **MemorySystem** - Ricorda entità viste
  - Decay time configurabile
  - Last known position
  - Threat level tracking (PerceptionMemory implementato)

#### Esempi Pratici
- [x] Esempi base disponibili in documentazione
- [x] Pattern AI con sensori e nodi avanzati
- [x] Esempi di utilizzo sensori (EntitySensor, BlockSensor, SoundSensor)

---

### Phase 2: Utility AI System ✅ COMPLETATO
**Priorità**: MEDIA  
**Completato**: 2025-11-09  
**Versione**: v0.2.0

#### Core Utility System
- [x] **Scorer Interface** - Sistema di punteggio per behaviors
  - Combinatori (add, multiply, scale, clamp, invert)
  - Scorer costanti e funzionali
  
- [x] **Consideration** - Valutazione singola variabile
  - Normalizzazione automatica input
  - Builder pattern per configurazione
  - Integrazione con ResponseCurve
  
- [x] **ResponseCurve** - Curve di valutazione
  - Linear, Quadratic, Cubic
  - Exponential con slope configurabile
  - Logistic (S-curve) con steepness
  - Step, SmoothStep, Power, Sine
  - Combinatori (invert, scale, offset, clamp, chain)
  
- [x] **UtilitySelector** - Seleziona behavior con score più alto
  - Re-evaluation interval configurabile
  - Cambio dinamico behavior
  - Debug con score tracking
  
- [x] **DynamicPrioritySelector** - Priorità che cambiano runtime
  - Riordino automatico children
  - Integrazione con Scorer
  - Priority tracking per debug

#### Esempi Completi
- [x] Combat AI con utility scoring (attack/flee/cover)
- [x] Resource gathering AI con inventory tracking
- [x] Task Manager AI con dynamic priorities
- [x] Guida completa curve di risposta
- [x] Best practices e performance tips

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

## 🗓️ Timeline

### Completate
- ✅ **v0.1.0-beta** (Core + Nodi + Sensori): Completato Nov 2025

### In Pianificazione
- **v0.2.0** (Utility AI): ~1 settimana
- **v0.3.0** (GOAP): ~2 settimane
- **v0.4.0** (Debug Tools): ~1 settimana
- **v0.5.0** (Pathfinding): ~2 settimane
- **v1.0.0** (Stable Release): ~1 mese
- **v2.0.0** (Advanced Features): ~2-3 mesi

**Tempo Stimato Rimanente**: ~2-3 mesi per v1.0.0 stable

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

---

## 📊 Progress Tracker

### Completamento Features per Versione

#### v0.1.0-beta ✅ 100% COMPLETATO
- ✅ Core Framework (7/7)
- ✅ Nodi Base (3/3)
- ✅ Nodi Avanzati (5/5)
- ✅ Sistema Percezione (6/6) - Tutti i sensori implementati
- ✅ Documentazione Completa

#### v0.2.0 (Utility AI) ✅ 100% COMPLETATO
- ✅ Scorer Interface (5/5)
- ✅ Consideration & ResponseCurve (13+ curves)
- ✅ UtilitySelector (1/1)
- ✅ DynamicPrioritySelector (1/1)
- ✅ Esempi e Documentazione (3 esempi completi)

#### v0.3.0 (GOAP) - 0%
- ⏳ In pianificazione

#### v0.4.0 (Debug Tools) - 0%
- ⏳ In pianificazione

### Overall Progress to v1.0.0 Stable
```
████████████████░░░░░░░░░░░░ 50% Complete

Phase 1: ████████████████████ 100% ✅ (Nodi + Sensori)
Phase 2: ████████████████████ 100% ✅ (Utility AI)
Phase 3: ░░░░░░░░░░░░░░░░░░░░   0% ⏳ (GOAP)
Phase 4: ░░░░░░░░░░░░░░░░░░░░   0% ⏳ (Debug Tools)
Phase 5: ░░░░░░░░░░░░░░░░░░░░   0% ⏳ (Pathfinding)
```

---

**Ultima Modifica**: 2025-11-09 (Completamento Phase 2)  
**Versione Corrente**: v0.2.0-beta (COMPLETA)  
**Prossima Release**: v0.3.0 (GOAP System)
