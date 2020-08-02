package thallium.fabric.interfaces;

import it.unimi.dsi.fastutil.objects.ObjectList;

public interface IWorldRenderer {

    public ObjectList<net.minecraft.client.render.WorldRenderer.ChunkInfo> getChunkInfo();

}