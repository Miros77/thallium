package me.isaiah.mods.fps.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import me.isaiah.mods.fps.interfaces.IThreadExecutor;
import net.minecraft.util.thread.ThreadExecutor;

@Mixin(ThreadExecutor.class)
public class MixinThreadExecutor implements IThreadExecutor {

    @Override
    @Shadow
    public void runTasks() {
    }

}
