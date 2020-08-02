package thallium.fabric.interfaces;

import java.util.concurrent.atomic.AtomicReference;

import net.minecraft.client.render.chunk.ChunkBuilder.BuiltChunk;
import net.minecraft.client.render.chunk.ChunkBuilder.ChunkData;

public interface IChunkInfo {

    /**
     * Get the built chunk's ChunkData
     * 
     * @author ThalliumMod
     */
    public AtomicReference<ChunkData> getBuiltChunkData();

    /**
     * Gets the built chunk
     * 
     * @author ThalliumMod
     */
    public BuiltChunk getBuiltChunk();

}