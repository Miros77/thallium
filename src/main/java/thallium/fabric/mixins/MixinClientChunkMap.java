package thallium.fabric.mixins;

import java.util.concurrent.atomic.AtomicReferenceArray;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.world.ClientChunkManager.ClientChunkMap;
import net.minecraft.world.chunk.WorldChunk;

@Mixin(ClientChunkMap.class)
public class MixinClientChunkMap {

    @Final
    @Shadow
    private AtomicReferenceArray<WorldChunk> chunks;

    @Final
    @Shadow
    private int radius;

    @Final
    @Shadow
    private int diameter;

    @Shadow
    private volatile int centerChunkX;
    @Shadow
    private volatile int centerChunkZ;

    @Shadow
    private int loadedChunkCount;

    @Inject(at = @At("HEAD"), method = "<init>", cancellable = true)
    public void init(int loadDistance, CallbackInfo ci) {
        this.radius = loadDistance;
        this.diameter = loadDistance * 2 + 1;
        this.chunks = new AtomicReferenceArray<WorldChunk>(this.diameter * this.diameter);
        ci.cancel(); // We Overwrite the constructor
    }

    @Shadow
    private int getIndex(int chunkX, int chunkZ) {
        return 0;
    }

    @Shadow
    protected void set(int index, WorldChunk chunk) {
    }

    @Shadow
    protected WorldChunk compareAndSet(int index, WorldChunk expect, WorldChunk update) {
        return null;
    }

    @Shadow
    private boolean isInRadius(int chunkX, int chunkZ) {
        return false;
    }

    @Shadow
    protected WorldChunk getChunk(int index) {
        return null;
    }

}