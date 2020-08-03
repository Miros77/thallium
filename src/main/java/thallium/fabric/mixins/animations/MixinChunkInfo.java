package thallium.fabric.mixins.animations;

import java.util.concurrent.atomic.AtomicReference;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.client.render.WorldRenderer.ChunkInfo;
import net.minecraft.client.render.chunk.ChunkBuilder;
import net.minecraft.client.render.chunk.ChunkBuilder.ChunkData;
import net.minecraft.util.math.Direction;
import thallium.fabric.interfaces.IChunkInfo;

@Mixin(ChunkInfo.class)
public class MixinChunkInfo implements IChunkInfo {

    @Shadow
    @Final
    private ChunkBuilder.BuiltChunk chunk;

    @Shadow
    @Final
    private Direction direction;

    /**
     * Gets the built chunk
     * 
     * @author ThalliumMod
     */
    @Override
    public ChunkBuilder.BuiltChunk getBuiltChunk() {
        return chunk;
    }

    /**
     * Get the built chunk's ChunkData
     * 
     * @author ThalliumMod
     */
    @Override
    public AtomicReference<ChunkData> getBuiltChunkData() {
        return chunk.data;
    }

}