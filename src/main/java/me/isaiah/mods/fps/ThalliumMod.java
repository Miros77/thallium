package me.isaiah.mods.fps;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.fabricmc.api.ModInitializer;

public class ThalliumMod implements ModInitializer {

    public Logger LOGGER = LogManager.getLogger("Thallium");

	@Override
	public void onInitialize() {
		LOGGER.info("Thallium Enabled");
	    LOGGER.info("- Game Renderer Optimized");
		LOGGER.info("- Chunk Manager Optimized");
	}

}