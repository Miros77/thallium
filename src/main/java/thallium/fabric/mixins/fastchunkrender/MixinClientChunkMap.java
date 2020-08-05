package thallium.fabric.mixins.fastchunkrender;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientChunkManager;
import net.minecraft.client.world.ClientChunkManager.ClientChunkMap;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.chunk.WorldChunk;
import thallium.fabric.chunk.FastChunkMap;
import thallium.fabric.gui.ThalliumOptions;
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
            fast = new FastChunkMap(loadDistance, c);
    }

    @Override
    public FastChunkMap getFastMap() {
        return fast;
    }

    @Override
    public void setFastMap(FastChunkMap fast) {
        this.fast = fast;
    }

    @Inject(at = @At("HEAD"), method = "getChunk", cancellable = true)
    public void getChunkFast(int index, CallbackInfoReturnable<WorldChunk> ci) {
        if (ThalliumOptions.useFastRenderer)
            ci.setReturnValue(fast.getChunk(index));
    }

    @Inject(at = @At("HEAD"), method = "set", cancellable = true)
    public void fastSet(int index, WorldChunk chunk, CallbackInfo ci) {
        if (ThalliumOptions.useFastRenderer) {
            fast.set(index, chunk);
            ci.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "compareAndSet", cancellable = true)
    public void compareAndSet(int index, WorldChunk expect, WorldChunk update, CallbackInfoReturnable<WorldChunk> ci) {
        if (ThalliumOptions.useFastRenderer)
            ci.setReturnValue(fast.getChunk(index));
    }

    @Inject(at = @At("HEAD"), method = "getIndex", cancellable = true)
    public void getIndex(int x, int z, CallbackInfoReturnable<Integer> ci) {
        if (ThalliumOptions.useFastRenderer)
            ci.setReturnValue((int) ChunkPos.toLong(x, z));
    }

    private ClientPlayerEntity player;

    @Override
    public boolean inRadius(int chunkX, int chunkZ) {
        if (null == this.player)
            player = MinecraftClient.getInstance().player;

        // NORTH = -z | SOUTH = +z
        // EAST  = +x | WEST  = -x
        boolean vanilla = Math.abs(chunkX - this.centerChunkX) <= this.radius && Math.abs(chunkZ - this.centerChunkZ) <= this.radius;
        if (!vanilla) return false;

        int offset = ThalliumOptions.directionalRender.level;
        if (offset == -1)
            return vanilla;

        Direction FACING = player.getHorizontalFacing();
        switch (FACING) {
            case NORTH:
                if (chunkZ-offset > this.centerChunkZ)
                    return false;
                break;
            case SOUTH:
                if (chunkZ+offset < this.centerChunkZ)
                    return false;
                break;
            case EAST:
                if (chunkX+offset < this.centerChunkX)
                    return false;
                break;
            case WEST:
                if (chunkX-offset > this.centerChunkX)
                    return false;
                break;
            default:
                return vanilla;
        }
        return vanilla;
    }

    @Shadow
    public WorldChunk getChunk(int index) {
        throw new IllegalArgumentException("Mixin stub");
    }

    @Override
    public WorldChunk getChunkByIndex(int index) {
        return getChunk(index);
    }

}