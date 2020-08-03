package thallium.fabric.gui;

import net.minecraft.client.options.BooleanOption;

public class ThalliumOptions {

    public static boolean useFastRenderer    = true;
    public static boolean useFastMath        = true;
    public static boolean optimizeAnimations = true;
    public static boolean renderSkip         = true;

    public static final BooleanOption FAST_RENDER = new BooleanOption("Use fast renderer", gameOptions -> useFastRenderer, (gameOptions, boolean_) -> {
        useFastRenderer = boolean_;
    });

    public static final BooleanOption FAST_MATH = new BooleanOption("Use fast math", gameOptions -> useFastMath, (gameOptions, boolean_) -> {
        useFastMath = boolean_;
    });

    public static final BooleanOption OPTIMIZE_ANIMATIONS = new BooleanOption("Optimize animations", gameOptions -> optimizeAnimations, (gameOptions, boolean_) -> {
        optimizeAnimations = boolean_;
    });

    public static final BooleanOption RENDER_SKIP = new BooleanOption("Skip long renders", gameOptions -> renderSkip, (gameOptions, boolean_) -> {
        renderSkip = boolean_;
    });


}