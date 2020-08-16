package thallium.fabric.mixins.old;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.CloudRenderMode;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.options.Option;

@Mixin(MinecraftClient.class)
public class MixinMinecraftClient {

    // Shadow Fields
    @Shadow public int fpsCounter;
    @Shadow public static int currentFps;
    @Shadow public GameOptions options;
    @Shadow public String fpsDebugString;
    @Shadow public long nextDebugInfoUpdateTime;

    private long lastDebugUpdateTime;

    @Inject(at = @At("HEAD"), method = "run")
    public void startNewThread(CallbackInfo ci) {
        debugUpdate(); // Redirect debug info onto separate thread
    }

    public void debugUpdate() {
        // Thallium_Mod - Update the FPS debug string not during the main render

        new Thread(() -> {
            while (true) {
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastDebugUpdateTime >= 1000) {
                    lastDebugUpdateTime = currentTime;
                    currentFps = this.fpsCounter;
                    this.fpsDebugString = String.format("%d fps T: %s%s%s%s B: %d", currentFps, (double)this.options.maxFps == Option.FRAMERATE_LIMIT.getMax() ? "inf" : Integer.valueOf(this.options.maxFps), this.options.enableVsync ? " vsync" : "", this.options.graphicsMode.toString(), this.options.cloudRenderMode == CloudRenderMode.OFF ? "" : (this.options.cloudRenderMode == CloudRenderMode.FAST ? " fast-clouds" : " fancy-clouds"), this.options.biomeBlendRadius);
                    this.fpsCounter = 0;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {e.printStackTrace();}
            }
        }, "Thallium F3 Loop").start();

        nextDebugInfoUpdateTime = Integer.MAX_VALUE; // Disable vanilla while() loop
    }

}