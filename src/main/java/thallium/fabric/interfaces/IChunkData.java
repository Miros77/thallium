package thallium.fabric.interfaces;

import java.util.Set;

import net.minecraft.client.texture.SpriteAtlasTexture;

public interface IChunkData {
    public Set<SpriteAtlasTexture> getVisibleTextures();

}