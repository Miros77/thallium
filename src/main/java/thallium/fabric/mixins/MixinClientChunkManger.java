package thallium.fabric.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.client.world.ClientChunkManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkManager;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.light.LightingProvider;
import thallium.fabric.interfaces.IChunkProvider;

@Mixin(ClientChunkManager.class)
public abstract class MixinClientChunkManger extends ChunkManager implements IChunkProvider {

    @Shadow
    public ClientWorld world;

    @Override
    public Chunk getChunk(int arg0, int arg1, ChunkStatus arg2, boolean arg3) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getDebugString() {
        // TODO Auto-generated method stub
        return "ThalliumChunkManager v2";
    }

    @Override
    public ClientWorld getWorld() {
        return world;
    }

}
