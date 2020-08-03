package thallium.fabric.mixins.old;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.render.GameRenderer;
import thallium.fabric.ThalliumMod;

@Mixin(GameRenderer.class)
public class MixinGameRenderer {

    @Inject(at = @At("HEAD"), method = "render", cancellable = true)
    public void render_start(float tickDelta, long startTime, boolean tick, CallbackInfo ci) {
        if (!ThalliumMod.doUpdate) ci.cancel();
    }

}