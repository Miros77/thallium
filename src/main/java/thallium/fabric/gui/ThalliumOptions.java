package thallium.fabric.gui;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import net.minecraft.client.options.BooleanOption;
import net.minecraft.client.options.CyclingOption;
import net.minecraft.text.LiteralText;
import thallium.fabric.ThalliumMod;
import thallium.fabric.math.FastMathType;

public class ThalliumOptions {

    public static boolean useFastRenderer    = true;
    public static boolean useFastMath        = true;
    public static boolean optimizeAnimations = true;
    public static boolean renderSkip         = true;
    public static boolean fastPlayerModel    = true;
    public static boolean optimizeHoppers    = true;

    public static EnumDirectionalRendering directionalRender = EnumDirectionalRendering.NORMAL;
    public static FastMathType fastMathType = FastMathType.RIVEN;

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

    public static final BooleanOption PLR_MODEL_OPTIMIZE = new BooleanOption("Fast Entity Model", gameOptions -> fastPlayerModel, (gameOptions, boolean_) -> {
        fastPlayerModel = boolean_;
        save();
    });

    public static final BooleanOption HOPPER_OPTIMIZE = new BooleanOption("Optimize Hopper Tick", gameOptions -> optimizeHoppers, (gameOptions, boolean_) -> {
        optimizeHoppers = boolean_;
        save();
    });


    public static CyclingOption DIRECTIONAL_RENDER;
    public static CyclingOption FAST_MATH_TYPE;

    public static void init() {
        DIRECTIONAL_RENDER = new CyclingOption("Directional Render", (options,integer) -> {
            directionalRender = directionalRender.ordinal() >= EnumDirectionalRendering.values().length-1 ? EnumDirectionalRendering.OFF :
                EnumDirectionalRendering.values()[directionalRender.ordinal()+1];
            save();
        }, (options,cyc) -> { return new LiteralText("Directional Render: " + directionalRender.name()); });

        FAST_MATH_TYPE = new CyclingOption("Math Algorithm", (options,integer) -> {
            fastMathType = fastMathType.ordinal() >= FastMathType.values().length-1 ? FastMathType.VANILLA : FastMathType.values()[fastMathType.ordinal()+1];
            save();
        }, (options,cyc) -> { return new LiteralText("Math Algorithm: " + (useFastMath ? fastMathType.name() : "Fast Math OFF")); });
    }

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
                if (str.equals("directionalRender"))
                    directionalRender = EnumDirectionalRendering.values()[Integer.valueOf(key)];
                if (str.equals("fastMathType"))
                    fastMathType = FastMathType.values()[Integer.valueOf(key)];
                if (str.equals("fastPlayerModel"))
                    fastPlayerModel = Boolean.valueOf(key);
                if (str.equals("optimizeHoppers"))
                    optimizeHoppers = Boolean.valueOf(key);
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
            map.put("directionalRender",  "" + directionalRender.ordinal());
            map.put("fastMathType",       "" + fastMathType.ordinal());
            map.put("fastPlayerModel",    "" + fastPlayerModel);
            map.put("optimizeHoppers",    "" + optimizeHoppers);

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