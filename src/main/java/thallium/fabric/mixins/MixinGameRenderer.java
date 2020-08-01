package thallium.fabric.mixins;

import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.client.render.GameRenderer;

@Mixin(GameRenderer.class)
public class MixinGameRenderer {

    //@Overwrite
    //public void render(float tickDelta, long startTime, boolean tick) {
    //}

}