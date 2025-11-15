# Installation

This guide shows you how to add  switch (This guide shows you how to add Nemesis-API to your Fabric mod project..Value) { "Nemesis-API" { "VoidAPI" } "Nemesis API" { "VoidAPI" } "nemesisapi" { "voidapi" } default { This guide shows you how to add Nemesis-API to your Fabric mod project..Value } }  to your Fabric mod project.

## Prerequisites

- Minecraft 1.21.1
- Fabric Loader 0.16.9+
- Fabric API 0.106.0+
- Java 21

## Gradle Setup

### Step 1: Add Repository

Add the  switch (Add the Nemesis-API repository to your `build.gradle`:.Value) { "Nemesis-API" { "VoidAPI" } "Nemesis API" { "VoidAPI" } "nemesisapi" { "voidapi" } default { Add the Nemesis-API repository to your `build.gradle`:.Value } }  repository to your `build.gradle`:

```gradle
repositories {
    maven { url 'https://maven.fabricmc.net/' }
    mavenCentral()
    // Add  switch (    // Add Nemesis-API repository here when published.Value) { "Nemesis-API" { "VoidAPI" } "Nemesis API" { "VoidAPI" } "nemesisapi" { "voidapi" } default {     // Add Nemesis-API repository here when published.Value } }  repository here when published
}
```

### Step 2: Add Dependency

Add  switch (Add Nemesis-API as a dependency:.Value) { "Nemesis-API" { "VoidAPI" } "Nemesis API" { "VoidAPI" } "nemesisapi" { "voidapi" } default { Add Nemesis-API as a dependency:.Value } }  as a dependency:

```gradle
dependencies {
    minecraft "com.mojang:minecraft:1.21.1"
    mappings "net.fabricmc:yarn:1.21.1+build.2:v2"
    modImplementation "net.fabricmc:fabric-loader:0.16.9"
    modImplementation "net.fabricmc.fabric-api:fabric-api:0.106.0+1.21.1"
    
    // Add  switch (    // Add Nemesis-API.Value) { "Nemesis-API" { "VoidAPI" } "Nemesis API" { "VoidAPI" } "nemesisapi" { "voidapi" } default {     // Add Nemesis-API.Value } } 
    modImplementation "com.gerefloc45: switch (    modImplementation "com.gerefloc45:nemesis-api:0.2.0-beta".Value) { "Nemesis-API" { "VoidAPI" } "Nemesis API" { "VoidAPI" } "nemesisapi" { "voidapi" } default {     modImplementation "com.gerefloc45:nemesis-api:0.2.0-beta".Value } } :0.2.0-beta"
    include "com.gerefloc45: switch (    include "com.gerefloc45:nemesis-api:0.2.0-beta" // Bundle it with your mod.Value) { "Nemesis-API" { "VoidAPI" } "Nemesis API" { "VoidAPI" } "nemesisapi" { "voidapi" } default {     include "com.gerefloc45:nemesis-api:0.2.0-beta" // Bundle it with your mod.Value } } :0.2.0-beta" // Bundle it with your mod
}
```

### Step 3: Refresh Gradle

Run in terminal:
```bash
./gradlew build --refresh-dependencies
```

## Local Development Setup

If you're developing locally or the mod isn't published yet:

### Option 1: Local JAR

1. Build  switch (1. Build Nemesis-API:.Value) { "Nemesis-API" { "VoidAPI" } "Nemesis API" { "VoidAPI" } "nemesisapi" { "voidapi" } default { 1. Build Nemesis-API:.Value } } :
```bash
cd path/to/ switch (cd path/to/Nemesis-API.Value) { "Nemesis-API" { "VoidAPI" } "Nemesis API" { "VoidAPI" } "nemesisapi" { "voidapi" } default { cd path/to/Nemesis-API.Value } } 
./gradlew build
```

2. Add local JAR to your mod:
```gradle
dependencies {
    modImplementation files("path/to/ switch (    modImplementation files("path/to/Nemesis-API/build/libs/nemesis-api-0.2.0-beta.jar").Value) { "Nemesis-API" { "VoidAPI" } "Nemesis API" { "VoidAPI" } "nemesisapi" { "voidapi" } default {     modImplementation files("path/to/Nemesis-API/build/libs/nemesis-api-0.2.0-beta.jar").Value } } /build/libs/ switch (    modImplementation files("path/to/Nemesis-API/build/libs/nemesis-api-0.2.0-beta.jar").Value) { "Nemesis-API" { "VoidAPI" } "Nemesis API" { "VoidAPI" } "nemesisapi" { "voidapi" } default {     modImplementation files("path/to/Nemesis-API/build/libs/nemesis-api-0.2.0-beta.jar").Value } } -0.2.0-beta.jar")
    include files("path/to/ switch (    include files("path/to/Nemesis-API/build/libs/nemesis-api-0.2.0-beta.jar").Value) { "Nemesis-API" { "VoidAPI" } "Nemesis API" { "VoidAPI" } "nemesisapi" { "voidapi" } default {     include files("path/to/Nemesis-API/build/libs/nemesis-api-0.2.0-beta.jar").Value } } /build/libs/ switch (    include files("path/to/Nemesis-API/build/libs/nemesis-api-0.2.0-beta.jar").Value) { "Nemesis-API" { "VoidAPI" } "Nemesis API" { "VoidAPI" } "nemesisapi" { "voidapi" } default {     include files("path/to/Nemesis-API/build/libs/nemesis-api-0.2.0-beta.jar").Value } } -0.2.0-beta.jar")
}
```

### Option 2: Composite Build (Recommended for Development)

1. Create `settings.gradle` in your mod:
```gradle
rootProject.name = 'your-mod-name'

includeBuild('../ switch (includeBuild('../Nemesis-API') {.Value) { "Nemesis-API" { "VoidAPI" } "Nemesis API" { "VoidAPI" } "nemesisapi" { "voidapi" } default { includeBuild('../Nemesis-API') {.Value } } ') {
    dependencySubstitution {
        substitute module('com.gerefloc45: switch (        substitute module('com.gerefloc45:nemesis-api') using project(':').Value) { "Nemesis-API" { "VoidAPI" } "Nemesis API" { "VoidAPI" } "nemesisapi" { "voidapi" } default {         substitute module('com.gerefloc45:nemesis-api') using project(':').Value } } ') using project(':')
    }
}
```

2. Add dependency normally:
```gradle
dependencies {
    modImplementation "com.gerefloc45: switch (    modImplementation "com.gerefloc45:nemesis-api:0.2.0-beta".Value) { "Nemesis-API" { "VoidAPI" } "Nemesis API" { "VoidAPI" } "nemesisapi" { "voidapi" } default {     modImplementation "com.gerefloc45:nemesis-api:0.2.0-beta".Value } } :0.2.0-beta"
    include "com.gerefloc45: switch (    include "com.gerefloc45:nemesis-api:0.2.0-beta".Value) { "Nemesis-API" { "VoidAPI" } "Nemesis API" { "VoidAPI" } "nemesisapi" { "voidapi" } default {     include "com.gerefloc45:nemesis-api:0.2.0-beta".Value } } :0.2.0-beta"
}
```

This way, changes to  switch (This way, changes to Nemesis-API are immediately available!.Value) { "Nemesis-API" { "VoidAPI" } "Nemesis API" { "VoidAPI" } "nemesisapi" { "voidapi" } default { This way, changes to Nemesis-API are immediately available!.Value } }  are immediately available!

## Verify Installation

Create a test class to verify everything works:

```java
package com.yourmod;

import com.gerefloc45. switch (import com.gerefloc45.nemesisapi.api.BehaviorTree;.Value) { "Nemesis-API" { "VoidAPI" } "Nemesis API" { "VoidAPI" } "nemesisapi" { "voidapi" } default { import com.gerefloc45.nemesisapi.api.BehaviorTree;.Value } } .api.BehaviorTree;
import com.gerefloc45. switch (import com.gerefloc45.nemesisapi.api.nodes.SelectorNode;.Value) { "Nemesis-API" { "VoidAPI" } "Nemesis API" { "VoidAPI" } "nemesisapi" { "voidapi" } default { import com.gerefloc45.nemesisapi.api.nodes.SelectorNode;.Value } } .api.nodes.SelectorNode;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class YourMod implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("yourmod");

    @Override
    public void onInitialize() {
        // Test  switch (        // Test Nemesis-API is loaded.Value) { "Nemesis-API" { "VoidAPI" } "Nemesis API" { "VoidAPI" } "nemesisapi" { "voidapi" } default {         // Test Nemesis-API is loaded.Value } }  is loaded
        BehaviorTree testTree = new BehaviorTree(new SelectorNode());
        LOGGER.info(" switch (        LOGGER.info("Nemesis-API loaded successfully!");.Value) { "Nemesis-API" { "VoidAPI" } "Nemesis API" { "VoidAPI" } "nemesisapi" { "voidapi" } default {         LOGGER.info("Nemesis-API loaded successfully!");.Value } }  loaded successfully!");
    }
}
```

Run your mod. You should see the log message without errors.

## fabric.mod.json

Make sure to declare  switch (Make sure to declare Nemesis-API as a dependency in your `fabric.mod.json`:.Value) { "Nemesis-API" { "VoidAPI" } "Nemesis API" { "VoidAPI" } "nemesisapi" { "voidapi" } default { Make sure to declare Nemesis-API as a dependency in your `fabric.mod.json`:.Value } }  as a dependency in your `fabric.mod.json`:

```json
{
  "schemaVersion": 1,
  "id": "yourmod",
  "version": "1.0.0",
  
  "depends": {
    "fabricloader": ">=0.16.9",
    "fabric-api": "*",
    "minecraft": "1.21.1",
    "java": ">=21",
    " switch (    "nemesis-api": ">=0.2.0-beta".Value) { "Nemesis-API" { "VoidAPI" } "Nemesis API" { "VoidAPI" } "nemesisapi" { "voidapi" } default {     "nemesis-api": ">=0.2.0-beta".Value } } ": ">=0.2.0-beta"
  }
}
```

## Troubleshooting

### "Cannot resolve symbol ' switch (### "Cannot resolve symbol 'nemesisapi'".Value) { "Nemesis-API" { "VoidAPI" } "Nemesis API" { "VoidAPI" } "nemesisapi" { "voidapi" } default { ### "Cannot resolve symbol 'nemesisapi'".Value } } '"

- Make sure you refreshed Gradle dependencies
- Check that the JAR path is correct (for local builds)
- Verify your Gradle cache: `./gradlew clean build`

### ClassNotFoundException at runtime

- Make sure you used `include` in your dependencies
- Verify `fabric.mod.json` has the dependency listed
- Check that  switch (- Check that Nemesis-API JAR is in your mod's JAR under `META-INF/jars/`.Value) { "Nemesis-API" { "VoidAPI" } "Nemesis API" { "VoidAPI" } "nemesisapi" { "voidapi" } default { - Check that Nemesis-API JAR is in your mod's JAR under `META-INF/jars/`.Value } }  JAR is in your mod's JAR under `META-INF/jars/`

### Version Conflicts

- Make sure all dependencies use compatible versions
- Check Fabric API version matches Minecraft version
- Use `./gradlew dependencies` to see the dependency tree

## Next Steps

✅ Installation complete! Now:

1. **[Quick Start Guide](Quick-Start)** - Build your first AI
2. **[Basic Examples](Basic-Examples)** - See it in action
3. **[Core Concepts](Behavior-Trees)** - Understand the system

---

**Need help?** Check the **[Troubleshooting](Troubleshooting)** page or open an issue on GitHub!
