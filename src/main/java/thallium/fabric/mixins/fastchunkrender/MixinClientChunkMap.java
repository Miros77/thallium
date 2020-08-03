package thallium.fabric.mixins.fastchunkrender;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.world.ClientChunkManager;
import net.minecraft.client.world.ClientChunkManager.ClientChunkMap;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.WorldChunk;
import thallium.fabric.chunk.FastChunkMap;
import thallium.fabric.interfaces.IChunkMap;

@Mixin(ClientChunkMap.class)
public class MixinClientChunkMap implements IChunkMap {

    public FastChunkMap fast;

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

    @Inject(at = @At("TAIL"), method = "<init>", cancellable = true)
    public void init(ClientChunkManager c, int loadDistance, CallbackInfo ci) {
        if (null == fast)
            fast = new FastChunkMap(loadDistance);
    }

    @Override
    public FastChunkMap getFastMap() {
        return fast;
    }

    @Override
    public void setFastMap(FastChunkMap fast) {
        this.fast = fast;
    }

    @Overwrite
    public WorldChunk getChunk(int index) {
        System.out.println("WARNING CALL TO OLD getChunk");
        return fast.getChunk(index);
    }

    @Overwrite
    public void set(int index, WorldChunk chunk) {
        fast.set(index, chunk);
    }

    @Overwrite
    public WorldChunk compareAndSet(int index, WorldChunk expect, WorldChunk update) {
        System.out.println("WARNING CALL TO OLD compareAndSet");
        return null;
    }

    @Overwrite
    public int getIndex(int x, int z) {
        System.out.println("WARNING CALL TO OLD getIndex");
        return (int) ChunkPos.toLong(x, z);
    }

    @Override
    public boolean inRadius(int chunkX, int chunkZ) {
        return Math.abs(chunkX - this.centerChunkX) <= this.radius && Math.abs(chunkZ - this.centerChunkZ) <= this.radius;
    }

}