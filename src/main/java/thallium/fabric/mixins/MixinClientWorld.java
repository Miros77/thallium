package thallium.fabric.mixins;

import thallium.fabric.chunk.ThalliumClientChunkManager;

import net.minecraft.client.world.ClientChunkManager;
import net.minecraft.client.world.ClientWorld;

import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ClientWorld.class)
public abstract class MixinClientWorld {

    @Dynamic
    @Redirect(method = "<init>", at = @At(value = "NEW", target = "net/minecraft/client/world/ClientChunkManager"))
    private static ClientChunkManager redirectChunkManager(ClientWorld w, int distance) {
        return new ThalliumClientChunkManager(w, distance);
    }

}