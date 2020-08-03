package thallium.fabric.mixins.animations;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.WorldRenderer.ChunkInfo;
import thallium.fabric.interfaces.IWorldRenderer;

@Mixin(WorldRenderer.class)
public class MixinWorldRenderer implements IWorldRenderer {

    @Shadow
    @Final
    private ObjectList<ChunkInfo> visibleChunks;


    @Override
    public ObjectList<ChunkInfo> getChunkInfo() {
        return visibleChunks;
    }

}