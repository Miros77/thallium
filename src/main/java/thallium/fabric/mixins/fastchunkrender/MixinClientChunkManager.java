package thallium.fabric.mixins.fastchunkrender;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.client.world.ClientChunkManager;
import net.minecraft.client.world.ClientChunkManager.ClientChunkMap;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.biome.source.BiomeArray;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkManager;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraft.world.chunk.light.LightingProvider;
import thallium.fabric.ThalliumMod;
import thallium.fabric.chunk.FastChunkMap;
import thallium.fabric.interfaces.IChunkMap;
import thallium.fabric.interfaces.IChunkProvider;

@Mixin(ClientChunkManager.class)
public abstract class MixinClientChunkManager extends ChunkManager implements IChunkProvider {

    @Shadow
    public ClientWorld world;

    @Shadow
    private volatile ClientChunkMap chunks;

    @Shadow
    @Final
    private WorldChunk emptyChunk;

    @Shadow
    public static boolean positionEquals(WorldChunk chunk, int x, int y) {throw new IllegalArgumentException("Mixin Stub");}

    public FastChunkMap fastMap;

    @Overwrite
    public void unload(int x, int z) {
        ((IChunkMap)this.chunks).getFastMap().unload(x, z);
    }

    @Override
    public String getDebugString() {
        return "ThalliumChunkManager: " + ((IChunkMap)this.chunks).getFastMap().fastChunks.size();
    }

    @Override
    public BlockView getWorld() {
        return world;
    }

    /**
     * Replace instances of ClientChunkMap in the vanilla method with our FastChunkMap
     */
    @Overwrite
    public void updateLoadDistance(int loadDistance) {
        int i = ((IChunkMap)this.chunks).getFastMap().radius;
        if (i != (Math.max(2, loadDistance) + 3)) {
            FastChunkMap clientChunkMap = new FastChunkMap(Math.max(2, loadDistance) + 3);
            ((IChunkMap)this.chunks).getFastMap().fastChunks.forEach((longKey,worldChunk) -> {
                if (((IChunkMap)this.chunks).inRadius(ChunkPos.getPackedX(longKey), ChunkPos.getPackedZ(longKey)))
                    clientChunkMap.set(longKey, worldChunk);
            });
            ((IChunkMap)this.chunks).setFastMap(clientChunkMap);
            fastMap = ((IChunkMap)this.chunks).getFastMap();
        }
    }

    /**
     * Replace instance of AtomicReference.
     */
    @Overwrite
    public WorldChunk loadChunkFromPacket(int x, int z, BiomeArray biomes, PacketByteBuf buf, CompoundTag tag, int i, boolean bl) {
        if (null == fastMap) fastMap = ((IChunkMap)this.chunks).getFastMap();
        long j = fastMap.getIndex(x, z);
        WorldChunk worldChunk = (WorldChunk)fastMap.getChunk(j); // ThalliumMod - Replace vanilla AtomicReference call
        if (bl || !positionEquals(worldChunk, x, z)) {
            if (biomes == null) {
                ThalliumMod.LOGGER.warn("Ignoring chunk since we don't have complete data: {}, {}", x, z);
                return null;
            }
            worldChunk = new WorldChunk(this.world, new ChunkPos(x, z), biomes);
            worldChunk.loadFromPacket(biomes, buf, tag, i);
            fastMap.set(j, worldChunk);
        } else worldChunk.loadFromPacket(biomes, buf, tag, i);

        ChunkSection[] chunkSections = worldChunk.getSectionArray();
        LightingProvider lightingProvider = this.getLightingProvider();
        lightingProvider.setLightEnabled(new ChunkPos(x, z), true);
        for (int k = 0; k < chunkSections.length; ++k) {
            ChunkSection chunkSection = chunkSections[k];
            lightingProvider.updateSectionStatus(ChunkSectionPos.from(x, k, z), ChunkSection.isEmpty(chunkSection));
        }
        this.world.resetChunkColor(x, z);
        return worldChunk;
    }

    @Override
    public Chunk getChunk(int i, int j, ChunkStatus chunkStatus, boolean bl) {
        if (null == fastMap) fastMap = ((IChunkMap)this.chunks).getFastMap();
        WorldChunk worldChunk;
        if (((IChunkMap)this.chunks).inRadius(i, j) && positionEquals(worldChunk = fastMap.getChunk(ChunkPos.toLong(i, j)), i, j))
            return worldChunk;

        if (bl) return this.emptyChunk;

        return null;
    }

}
