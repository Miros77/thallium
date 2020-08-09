package thallium.fabric.mixins.old;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import thallium.fabric.ThalliumMod;
import thallium.fabric.gui.ThalliumOptions;
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

    private int a = 0;
    private int b = 0;

    private long lastDebugUpdateTime;

    @Inject(at = @At("HEAD"), method = "run")
    public void startNewThread(CallbackInfo ci) {
        debugUpdate(); // Redirect debug info onto separate thread
    }

    @Inject(at = @At("TAIL"), method = "render")
    public void renderTail(boolean tick, CallbackInfo ci) {
        if (!ThalliumOptions.renderSkip) {
            a++;
            return;
        }

        long took = System.currentTimeMillis();

        // Thallium_Mod - This is pretty much disabled now
        if (took - last > 60) {
            ThalliumMod.doUpdate = true;
            last = took;
        }
        if ((ThalliumMod.doUpdate = a <= 60 || (a/2)-16 < b)) a++; else b++;
    }

    public void debugUpdate() {
        // Thallium_Mod - Update the FPS debug string not during render

        new Thread(() -> {
            while (true) {
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastDebugUpdateTime >= 1000) {
                    lastDebugUpdateTime = currentTime;
                    currentFps = this.fpsCounter;
                    this.fpsDebugString = String.format("%d fps T: %s%s%s%s B: %d" + (this.options.debugProfilerEnabled ? " D:" + a + " A:" + b : ""), currentFps, (double)this.options.maxFps == Option.FRAMERATE_LIMIT.getMax() ? "inf" : Integer.valueOf(this.options.maxFps), this.options.enableVsync ? " vsync" : "", this.options.graphicsMode.toString(), this.options.cloudRenderMode == CloudRenderMode.OFF ? "" : (this.options.cloudRenderMode == CloudRenderMode.FAST ? " fast-clouds" : " fancy-clouds"), this.options.biomeBlendRadius);
                    this.fpsCounter = 0;
                    this.a = 0;
                    this.b = 0;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {e.printStackTrace();}
            }
        }, "Thallium F3 Loop").start();

        nextDebugInfoUpdateTime = Integer.MAX_VALUE; // Disable vanilla while() loop
    }

}
