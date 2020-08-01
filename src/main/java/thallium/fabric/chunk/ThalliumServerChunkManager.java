package thallium.fabric.chunk;

import java.util.concurrent.Executor;
import java.util.function.Supplier;

import com.mojang.datafixers.DataFixer;

import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.server.WorldGenerationProgressListener;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructureManager;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.level.storage.LevelStorage.Session;

public class ThalliumServerChunkManager extends ServerChunkManager {

    private Long2ObjectOpenHashMap<WorldChunk> worldChunks;

    public ThalliumServerChunkManager(ServerWorld w, Session se, DataFixer df, StructureManager sm, Executor e, ChunkGenerator g,
            int vd, boolean bl, WorldGenerationProgressListener wgpl, Supplier<PersistentStateManager> su) {
        super(w, se, df, sm, e, g, vd, bl, wgpl, su);

        this.worldChunks = new Long2ObjectOpenHashMap<>(((((Math.max(2, vd) + 3) * 2) + 1))^2, Hash.FAST_LOAD_FACTOR);
    }

    public int getTotalChunksLoadedCount() {
        return this.threadedAnvilChunkStorage.getTotalChunksLoadedCount();
    }

    @Override
    public void applyViewDistance(int loadDistance) {
        super.applyViewDistance(loadDistance);
        Long2ObjectOpenHashMap<WorldChunk> copy = new Long2ObjectOpenHashMap<>(((((Math.max(2, loadDistance) + 3) * 2) + 1))^2, Hash.FAST_LOAD_FACTOR);
        this.worldChunks = copy;
    }

    @Override
    public Chunk getChunk(int x, int z, ChunkStatus leastStatus, boolean create) {
        long l = ChunkPos.toLong(x, z);
        if (worldChunks.containsKey(l))
            return worldChunks.get(l);

        return super.getChunk(x, z, leastStatus, create);
    }

    @Override
    public WorldChunk getWorldChunk(int chunkX, int chunkZ) {
        long l = ChunkPos.toLong(chunkX, chunkZ);
        if (worldChunks.containsKey(l))
            return worldChunks.get(l);
        WorldChunk c = super.getWorldChunk(chunkX, chunkZ);
        worldChunks.put(l, c);
        return c;
    }

    @Override
    public BlockView getChunk(int chunkX, int chunkZ) {
        return super.getChunk(chunkX, chunkZ);
    }

    @Override
    public String getDebugString() {
        return "ServerChunkCache: V=" + this.getLoadedChunkCount() + ",T=" + worldChunks.size();
    }

    @Override
    public int getLoadedChunkCount() {
        return this.threadedAnvilChunkStorage.getLoadedChunkCount();
    }

}