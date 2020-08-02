package thallium.fabric.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import thallium.fabric.ThalliumMod;
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

    private long last;
    private boolean isFpsThreadRunning;

    private int a = 0;
    private int b = 0;
    private int c = 0;

    private long lastFpsUpdateTime;

    @Inject(at = @At("TAIL"), method = "render")
    public void renderTail(boolean tick, CallbackInfo ci) {
        fpsUpdate(); // Start F3 debug menu update

        long took = System.currentTimeMillis();
        int skip = 55;
        if (currentFps > 400) skip = 50;
        if (currentFps > 500) skip = 40;
        if (currentFps > 600) skip = 30;

        // Thallium_Mod
        // If FPS is low, lower amount of processing to help get FPS back up.
        if (took - last > skip) {
            ThalliumMod.doUpdate = true;
            last = took;
            a++;
        } else {
            ThalliumMod.doUpdate = c == 0 || a <= 60;
            if (ThalliumMod.doUpdate) a++; else b++;
            if (c > 200) c = 0; else c++;
        }
    }

    public void fpsUpdate() {
        if (isFpsThreadRunning) return;
        isFpsThreadRunning = true;

        // Thallium_Mod - Update the FPS debug string not during render time

        new Thread(() -> {
            while (true) {
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastFpsUpdateTime >= 1000) {
                    lastFpsUpdateTime = currentTime;
                    currentFps = this.fpsCounter;
                    this.fpsDebugString = String.format("%d fps @" + (a + "-" + b/10) + " T: %s%s%s%s B: %d. +Thallium", currentFps, (double)this.options.maxFps == Option.FRAMERATE_LIMIT.getMax() ? "inf" : Integer.valueOf(this.options.maxFps), this.options.enableVsync ? " vsync" : "", this.options.graphicsMode.toString(), this.options.cloudRenderMode == CloudRenderMode.OFF ? "" : (this.options.cloudRenderMode == CloudRenderMode.FAST ? " fast-clouds" : " fancy-clouds"), this.options.biomeBlendRadius);
                    this.fpsCounter = 0;
                    this.a = 0;
                    this.b = 0;
                    this.c = 0;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {e.printStackTrace();}
            }
        }, "Thallium FPS Loop").start();

        nextDebugInfoUpdateTime = Integer.MAX_VALUE; // Disable vanilla while() loop
    }

}
