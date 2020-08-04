package thallium.fabric.mixins.fastchunkrender;

import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.server.world.ServerWorld;

@Mixin(ServerWorld.class)
public abstract class MixinServerWorld {

    // TODO See issue
    //@Dynamic
   // @Redirect(method = "<init>", at = @At(value = "NEW", target = "net/minecraft/server/world/ServerChunkManager"))
    //private static ServerChunkManager redirectChunkManager(ServerWorld w, Session se, DataFixer df, StructureManager sm, Executor e, ChunkGenerator g,
    //        int vd, boolean bl, WorldGenerationProgressListener wgpl, Supplier<PersistentStateManager> su) {
     //   return new ThalliumServerChunkManager(w, se, df, sm, e, g, vd, bl, wgpl, su);
    //}

}