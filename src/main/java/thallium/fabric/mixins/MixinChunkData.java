package thallium.fabric.mixins;

import java.util.HashSet;
import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.client.render.chunk.ChunkBuilder;
import net.minecraft.client.texture.SpriteAtlasTexture;

import thallium.fabric.interfaces.IChunkData;

@Mixin(ChunkBuilder.ChunkData.class)
public class MixinChunkData implements IChunkData {

    public Set<SpriteAtlasTexture> visibleTextures = new HashSet<>();

    @Override
    public Set<SpriteAtlasTexture> getVisibleTextures() {
        return visibleTextures;
    }

}
