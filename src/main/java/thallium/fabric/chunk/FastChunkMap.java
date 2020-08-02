package thallium.fabric.chunk;

import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.WorldChunk;

/**
 * Modified {@link net.minecraft.client.world.ClientChunkManager.ClientChunkMap}
 * with instances of Atomic replaced with fastutil's Long2ObjectOpenHashMap
 * 
 * @author ThalliumMod
 */
public class FastChunkMap {

    public Long2ObjectOpenHashMap<WorldChunk> fastChunks;

    public int radius;

    public int diameter;

    public int loadedChunkCount;

    public FastChunkMap(int loadDistance) {
        this.radius = loadDistance;
        this.diameter = loadDistance * 2 + 1;
        this.fastChunks = new Long2ObjectOpenHashMap<>(this.diameter ^ 2);
    }

    public WorldChunk getChunk(long index) {
        return this.fastChunks.get(index);
    }

    public void set(long index, WorldChunk chunk) {
        WorldChunk worldChunk = this.fastChunks.put(index, chunk);
        if (worldChunk != null) {
            --this.loadedChunkCount;
            //((ClientWorld)((IChunkProvider)(Object)this).getWorld()).unloadBlockEntities(worldChunk);
        }
    }

    public long getIndex(int x, int z) {
        return ChunkPos.toLong(x, z);
    }

    public void unload(int x, int z) {
        this.fastChunks.remove(ChunkPos.toLong(x, z));
    }

}