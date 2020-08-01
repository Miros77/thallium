package thallium.chunk;

import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.client.world.ClientChunkManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.world.biome.source.BiomeArray;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.EmptyChunk;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraft.world.chunk.light.LightingProvider;

public class ThalliumClientChunkManager extends ClientChunkManager {

    private final ClientWorld world;
    private final WorldChunk empty;

    private Long2ObjectOpenHashMap<WorldChunk> chunks;
    private int centerX, centerZ, radius;

    public ThalliumClientChunkManager(ClientWorld world, int renderDistance) {
        super(world, renderDistance);

        this.world = world;
        this.empty = new EmptyChunk(world, new ChunkPos(0, 0));
        this.radius = (Math.max(2, renderDistance) + 3);
        this.chunks = new Long2ObjectOpenHashMap<>((((this.radius * 2) + 1))^2, Hash.FAST_LOAD_FACTOR);
    }

    @Override
    public void unload(int x, int z) {
        this.chunks.remove(ChunkPos.toLong(x, z));
    }

    @Override
    public WorldChunk getChunk(int x, int z, ChunkStatus status, boolean createChunk) {
        WorldChunk chunk = this.chunks.get(ChunkPos.toLong(x, z));
        return chunk == null ? (createChunk ? empty : null) : chunk;
    }

    @Override
    public WorldChunk loadChunkFromPacket(int x, int z, BiomeArray biomeArray, PacketByteBuf buf, CompoundTag tag, int i, boolean bl) {
        if (notInRadius(x,z))
            return null;
        long key = ChunkPos.toLong(x, z);
        WorldChunk chunk = this.chunks.get(key);

        if (chunk == null && bl) {
            if (biomeArray == null) return null;
            this.chunks.put(key, (chunk = new WorldChunk(this.world, new ChunkPos(x, z), biomeArray)));
        }
        chunk.loadFromPacket(biomeArray, buf, tag, i);

        LightingProvider lightingProvider = this.getLightingProvider();
        lightingProvider.setLightEnabled(new ChunkPos(x, z), true);

        ChunkSection[] sections = chunk.getSectionArray();
        for (int y = 0; y < sections.length; ++y)
            lightingProvider.updateSectionStatus(ChunkSectionPos.from(x,y,z), ChunkSection.isEmpty(sections[y]));

        this.world.resetChunkColor(x, z);
        return chunk;
    }

    @Override
    public void setChunkMapCenter(int x, int z) {
        this.centerX = x;
        this.centerZ = z;
    }

    @Override
    public void updateLoadDistance(int loadDistance) {
        this.radius = Math.max(2, loadDistance) + 3;
        Long2ObjectOpenHashMap<WorldChunk> copy = new Long2ObjectOpenHashMap<>((((this.radius * 2) + 1))^2, Hash.FAST_LOAD_FACTOR);
        this.chunks.forEach((longKey,value) -> { if (!notInRadius(ChunkPos.getPackedX(longKey), ChunkPos.getPackedZ(longKey))) copy.put(longKey.longValue(), value); });
        this.chunks = copy;
    }

    private boolean notInRadius(int chunkX, int chunkZ) {
        return !(Math.abs(chunkX - this.centerX) <= this.radius && Math.abs(chunkZ - this.centerZ) <= this.radius);
    }

    @Override
    public String getDebugString() {
        return "ThalliumClientChunkCache: " + this.getLoadedChunkCount();
    }

    @Override
    public int getLoadedChunkCount() {
        return this.chunks.size();
    }

}