package thallium.fabric.interfaces;

import net.minecraft.world.chunk.WorldChunk;
import thallium.fabric.chunk.FastChunkMap;

public interface IChunkMap {

    public FastChunkMap getFastMap();

    public void setFastMap(FastChunkMap fast);

    public boolean inRadius(int x, int z);

    public WorldChunk getChunkByIndex(int index);

    public void setUpdating(boolean bl);

}