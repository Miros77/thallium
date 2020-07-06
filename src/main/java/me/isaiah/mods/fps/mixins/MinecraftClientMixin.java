package me.isaiah.mods.fps.mixins;

import java.util.Queue;
import java.util.concurrent.CompletableFuture;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import com.mojang.blaze3d.systems.RenderSystem;

import me.isaiah.mods.fps.interfaces.IThreadExecutor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gui.screen.Overlay;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.SplashScreen;
import net.minecraft.client.options.CloudRenderMode;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.options.Option;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.util.TickDurationMonitor;
import net.minecraft.util.Util;
import net.minecraft.util.profiler.ProfileResult;
import net.minecraft.util.profiler.Profiler;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    @Shadow
    public Profiler profiler;

    @Shadow
    public Window window;

    @Shadow
    public boolean paused;

    @Shadow
    public int fpsCounter;

    @Shadow
    public static int currentFps;

    @Shadow
    public GameOptions options;

    @Shadow
    public String fpsDebugString;

    @Shadow
    public long nextDebugInfoUpdateTime;

    @Shadow
    private CompletableFuture<Void> resourceReloadFuture;

    @Shadow
    public Screen currentScreen;

    @Shadow
    public Overlay overlay;

    @Shadow
    private Queue<Runnable> renderTaskQueue;

    @Shadow
    private RenderTickCounter renderTickCounter;

    @Shadow
    private float pausedTickDelta;

    @Shadow
    private IntegratedServer server;

    @Shadow
    private Mouse mouse;

    @Shadow
    private SoundManager soundManager;

    @Shadow
    public GameRenderer gameRenderer;

    @Shadow
    public Framebuffer framebuffer;

    @Shadow
    public boolean skipGameRender;

    @Shadow
    public ToastManager toastManager;

    @Shadow
    private ProfileResult tickProfilerResult;

    @Shadow
    public boolean shouldMonitorTickDuration() {
        return false;
    }

    @Shadow
    public boolean isIntegratedServerRunning() {
        return false;
    }

    @Shadow
    public int getFramerateLimit() {
        return 0;
    }

    @Shadow
    public void scheduleStop() {}

    @Shadow
    public void tick() {}

    @Shadow
    private void drawProfilerResults(MatrixStack matrixStack, ProfileResult profileResult) {}

    @Shadow
    public CompletableFuture<Void> reloadResources() {return null;}

    @Shadow
    public void startMonitor(boolean a, TickDurationMonitor b) {
    }

    @Shadow
    public void endMonitor(boolean a, TickDurationMonitor b) {
    }

    private boolean doUpdate = false;
    private long last;

    @Overwrite
    private void render(boolean tick) {
        boolean bl;
        Runnable runnable;
        this.window.setPhase("Pre render");

        long l = Util.getMeasuringTimeNano();
        if (this.window.shouldClose()) {
            this.scheduleStop();
        }
        if (this.resourceReloadFuture != null && !(this.overlay instanceof SplashScreen)) {
            CompletableFuture<Void> completableFuture = this.resourceReloadFuture;
            this.resourceReloadFuture = null;
            this.reloadResources().thenRun(() -> completableFuture.complete(null));
        }
        while ((runnable = this.renderTaskQueue.poll()) != null)
            runnable.run();

        if (tick) {
            int i = this.renderTickCounter.beginRenderTick(Util.getMeasuringTimeMs());
            ((IThreadExecutor)(Object)this).runTasks();
            for (int j = 0; j < Math.min(10, i); ++j)
                this.tick();

        }
        this.mouse.updateMouse();
        this.window.setPhase("Render");
        this.soundManager.updateListenerPosition(this.gameRenderer.getCamera());
        RenderSystem.pushMatrix();
        RenderSystem.clear(16640, false);
        this.framebuffer.beginWrite(true);
        BackgroundRenderer.method_23792();
        RenderSystem.enableTexture();
        RenderSystem.enableCull();

        if (!this.skipGameRender) {
            if (doUpdate) this.profiler.swap("gameRenderer");
            if (doUpdate) this.gameRenderer.render(this.paused ? this.pausedTickDelta : this.renderTickCounter.tickDelta, l, tick);
            this.toastManager.draw(new MatrixStack());
        }

        if (this.tickProfilerResult != null)
            this.drawProfilerResults(new MatrixStack(), this.tickProfilerResult);

        this.framebuffer.endWrite();
        RenderSystem.popMatrix();
        RenderSystem.pushMatrix();


        this.framebuffer.draw(this.window.getFramebufferWidth(), this.window.getFramebufferHeight());

        RenderSystem.popMatrix();
        this.window.swapBuffers();

        this.window.setPhase("Post render");
        ++this.fpsCounter;
        boolean bl2 = bl = this.isIntegratedServerRunning() && (this.currentScreen != null && this.currentScreen.isPauseScreen() || this.overlay != null && this.overlay.pausesGame()) && !this.server.isRemote();
        if (this.paused != bl) {
            if (this.paused) {
                this.pausedTickDelta = this.renderTickCounter.tickDelta;
            } else
                this.renderTickCounter.tickDelta = this.pausedTickDelta;
            this.paused = bl;
        }

        int width = this.window.getWidth();
        int height = this.window.getWidth();

        int mode = 0;
        if (width > 1200 && height > 800) mode = 1;

        while (Util.getMeasuringTimeMs() >= this.nextDebugInfoUpdateTime + 1000L) {
            currentFps = this.fpsCounter;
            this.fpsDebugString = String.format("%d fps T: %s%s%s%s B: %d", currentFps, (double)this.options.maxFps == Option.FRAMERATE_LIMIT.getMax() ? "inf" : Integer.valueOf(this.options.maxFps), this.options.enableVsync ? " vsync" : "", this.options.graphicsMode.toString(), this.options.cloudRenderMode == CloudRenderMode.OFF ? "" : (this.options.cloudRenderMode == CloudRenderMode.FAST ? " fast-clouds" : " fancy-clouds"), this.options.biomeBlendRadius);
            this.nextDebugInfoUpdateTime += 1000L;
            this.fpsCounter = 0;
        }

        long took = (System.currentTimeMillis());
        if (took - last > (mode == 0 ? 70 : 80)) {
            doUpdate = true;
            last = took;
        } else doUpdate = false;
    }

}
