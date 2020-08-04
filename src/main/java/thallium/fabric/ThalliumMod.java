package thallium.fabric;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import thallium.fabric.gui.ThalliumOptions;

public class ThalliumMod implements ModInitializer {

    public static String VERSION = "${version}";
    public static Logger LOGGER = LogManager.getLogger("Thallium");

    public static boolean doUpdate = false;
    public static File saveFile;

	@Override
	public void onInitialize() {
        LOGGER.info("Thallium " + VERSION + " Enabled.");

        saveFile = new File(FabricLoader.getInstance().getConfigDirectory(), "thallium-options.dat");
        if (saveFile.exists())
            ThalliumOptions.load();
        ThalliumOptions.save();

        if (ThalliumUpdateCheck.check(this))
            LOGGER.info("Outdated Thallium! For the latest updates please redownload Thallium from CurseForge!");
    }

}