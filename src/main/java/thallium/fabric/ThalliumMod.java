package thallium.fabric;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.fabricmc.api.ModInitializer;

public class ThalliumMod implements ModInitializer {

    public static Logger LOGGER = LogManager.getLogger("Thallium");

	@Override
	public void onInitialize() {
        LOGGER.info("Thallium Enabled");
        LOGGER.info("- Optimized Game Renderer");
        LOGGER.info("- Optimized Chunk Manager");
        LOGGER.info("- Optimized Animated Sprites");
    }

}