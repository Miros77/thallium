package thallium.fabric.mixins;

import java.util.List;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import thallium.fabric.interfaces.IChunkData;
import thallium.fabric.interfaces.IChunkInfo;
import thallium.fabric.interfaces.ISprite;
import thallium.fabric.interfaces.IWorldRenderer;

/**
 * Ports VannilaFix's Forge 1.12.2 Patch
 * 
 * @reason Replaces the updateAnimations method to only tick animated textures
 * that are in one of the loaded RenderChunks. This can lead to an FPS more than
 * three times higher on large modpacks with many textures.
 */
@Mixin(SpriteAtlasTexture.class)
public abstract class MixinSpriteAtlasTexture extends AbstractTexture {

    @Shadow
    @Final
    private List<Sprite> animatedSprites;

    /**
     * @author
     * @reason Optimized
     */
    @Overwrite
    public void tickAnimatedSprites() {
        MinecraftClient mc = MinecraftClient.getInstance();
        mc.getProfiler().push("determineVisibleTextures");

        for (net.minecraft.client.render.WorldRenderer.ChunkInfo renderInfo : ((IWorldRenderer)mc.worldRenderer).getChunkInfo())
            for (SpriteAtlasTexture texture : ((IChunkData) ((IChunkInfo)renderInfo).getBuiltChunk().data.get()).getVisibleTextures())
                ((ISprite) texture).markNeedsAnimationUpdate();


        GlStateManager.bindTexture(getGlId());
        for (Sprite texture : animatedSprites) {
            if (((ISprite) texture).needsAnimationUpdate()) {
                texture.tickAnimation();
                ((ISprite) texture).unmarkNeedsAnimationUpdate();
            }
        }
        mc.getProfiler().pop();
    }

}