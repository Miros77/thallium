package thallium.fabric;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.fabricmc.api.ModInitializer;

public class ThalliumMod implements ModInitializer {

    public static String VERSION = "${version}";
    public static Logger LOGGER = LogManager.getLogger("Thallium");

    public static boolean doUpdate = false;

	@Override
	public void onInitialize() {
        LOGGER.info("Thallium " + VERSION + " Enabled.");
        LOGGER.info("- Optimized Game Renderer");
        LOGGER.info("- Optimized Chunk Manager");
        LOGGER.info("- Optimized Animated Sprites");

        if (ThalliumUpdateCheck.check(this))
            LOGGER.info("Outdated Thallium! For the latest updates please redownload Thallium from CurseForge!");
    }

}