package thallium.fabric.mixins;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.util.math.MathHelper;
import thallium.fabric.MathUtils;

@Mixin(MathHelper.class)
public class MixinMathHelper {

    @Shadow
    @Final
    private static float[] SINE_TABLE;

    @Overwrite
    public static float sin(float value) {
        return MathUtils.useFastMath ? MathUtils.fastSin(value) : SINE_TABLE[(int)(value * 10430.378F) & 65535];
    }

    @Overwrite
    public static float cos(float value) {
        return MathUtils.useFastMath ? MathUtils.fastCos(value)  : SINE_TABLE[(int)(value * 10430.378F + 16384.0F) & 65535];
    }

}