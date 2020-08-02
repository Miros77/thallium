package thallium.fabric.interfaces;

import thallium.fabric.chunk.FastChunkMap;

public interface IChunkMap {

    public FastChunkMap getFastMap();

    public void setFastMap(FastChunkMap fast);

    public boolean inRadius(int x, int z);

}