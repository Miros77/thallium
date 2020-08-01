package me.isaiah.mods.fps.mixins;

import java.util.concurrent.Executor;
import java.util.function.Supplier;

import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.mojang.datafixers.DataFixer;

import net.minecraft.server.WorldGenerationProgressListener;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructureManager;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.level.storage.LevelStorage.Session;

import thallium.chunk.ThalliumServerChunkManager;

@Mixin(ServerWorld.class)
public abstract class MixinServerWorld {

    @Dynamic
    @Redirect(method = "<init>", at = @At(value = "NEW", target = "net/minecraft/server/world/ServerChunkManager"))
    private static ServerChunkManager redirectChunkManager(ServerWorld w, Session se, DataFixer df, StructureManager sm, Executor e, ChunkGenerator g,
            int vd, boolean bl, WorldGenerationProgressListener wgpl, Supplier<PersistentStateManager> su) {
        return new ThalliumServerChunkManager(w, se, df, sm, e, g, vd, bl, wgpl, su);
    }

}