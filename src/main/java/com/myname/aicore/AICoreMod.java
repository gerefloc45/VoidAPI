package com.myname.aicore;

import com.myname.aicore.core.BrainTicker;
import com.myname.aicore.util.AsyncHelper;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main entrypoint for the AI-Core framework mod.
 * This mod provides a complete AI framework for Minecraft entities.
 * It contains no gameplay content and is designed to be used as a library.
 * 
 * @author AI-Core Framework
 * @version 1.0.0
 */
public class AICoreMod implements ModInitializer {
    public static final String MOD_ID = "aicore";
    public static final String MOD_NAME = "AI-Core";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing {} Framework", MOD_NAME);

        // Initialize the brain ticker system
        BrainTicker.initialize();

        LOGGER.info("{} Framework initialized successfully", MOD_NAME);
        LOGGER.info("API ready for use by dependent mods");
    }

    /**
     * Called when the mod is being shut down.
     * Cleans up resources.
     */
    public static void shutdown() {
        LOGGER.info("Shutting down {} Framework", MOD_NAME);
        AsyncHelper.shutdown();
    }
}
