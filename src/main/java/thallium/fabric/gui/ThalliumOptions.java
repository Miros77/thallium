package thallium.fabric.gui;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import net.minecraft.client.options.BooleanOption;
import thallium.fabric.ThalliumMod;

public class ThalliumOptions {

    public static boolean useFastRenderer    = true;
    public static boolean useFastMath        = true;
    public static boolean optimizeAnimations = true;
    public static boolean renderSkip         = true;

    public static final BooleanOption FAST_RENDER = new BooleanOption("Use fast renderer", gameOptions -> useFastRenderer, (gameOptions, boolean_) -> {
        useFastRenderer = true; // TODO: Fix problems with disabling fast render
        save();
    });

    public static final BooleanOption FAST_MATH = new BooleanOption("Use fast math", gameOptions -> useFastMath, (gameOptions, boolean_) -> {
        useFastMath = boolean_;
        save();
    });

    public static final BooleanOption OPTIMIZE_ANIMATIONS = new BooleanOption("Optimize animations", gameOptions -> optimizeAnimations, (gameOptions, boolean_) -> {
        optimizeAnimations = boolean_;
        save();
    });

    public static final BooleanOption RENDER_SKIP = new BooleanOption("Skip long renders", gameOptions -> renderSkip, (gameOptions, boolean_) -> {
        renderSkip = boolean_;
        save();
    });


    @SuppressWarnings("unchecked")
    public static void load() {
        System.out.println("Loading saved options ...");
        long start = System.currentTimeMillis();

        try {
            FileInputStream fis = new FileInputStream(ThalliumMod.saveFile);
            ObjectInputStream ois = new ObjectInputStream(fis);
            HashMap<String,String> map = (HashMap<String, String>) ois.readObject();
            for (String str : map.keySet()) {
                String key = map.get(str);
                if (str.equals("fastRender"))
                    useFastRenderer = Boolean.valueOf(key);
                if (str.equals("fastmath"))
                    useFastMath = Boolean.valueOf(key);
                if (str.equals("optimizeAnimations"))
                    optimizeAnimations = Boolean.valueOf(key);
                if (str.equals("renderSkip"))
                    renderSkip = Boolean.valueOf(key);
            }
            ois.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println("Loaded thallium options in " + (System.currentTimeMillis()-start) + "ms");
    }

    public static void save() {
        System.out.println("Saving thallium options ...");
        long start = System.currentTimeMillis();

        try {
            HashMap<String,String> map = new HashMap<>();
            map.put("fastRender",         "" + useFastRenderer);
            map.put("fastmath",           "" + useFastMath);
            map.put("optimizeAnimations", "" + optimizeAnimations);
            map.put("renderSkip",         "" + renderSkip);

            ThalliumMod.saveFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(ThalliumMod.saveFile);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(map);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Saved thallium options in " + (System.currentTimeMillis()-start) + "ms");
    }


}