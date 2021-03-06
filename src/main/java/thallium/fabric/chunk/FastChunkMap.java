package thallium.fabric.chunk;

import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.client.world.ClientChunkManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
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

    public World world;

    public FastChunkMap(int loadDistance, ClientChunkManager c) {
        this.radius = loadDistance;
        this.diameter = (loadDistance * 2) + 1;
        this.world = (World) c.getWorld();
        this.fastChunks = new Long2ObjectOpenHashMap<>(this.diameter ^ 2);
    }

    public WorldChunk getChunk(long index) {
        return this.fastChunks.get(index);
    }

    public void set(long index, WorldChunk chunk) {
        WorldChunk worldChunk = this.fastChunks.put(index, chunk);
       if (worldChunk != null) {
            --this.loadedChunkCount;
            ((ClientWorld)world).unloadBlockEntities(worldChunk);
        } else {
            this.loadedChunkCount++;
        }
    }

    public void unload(int x, int z) {
        this.fastChunks.remove(ChunkPos.toLong(x, z));
    }

}