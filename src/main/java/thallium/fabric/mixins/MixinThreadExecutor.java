package thallium.fabric.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.util.thread.ThreadExecutor;
import thallium.fabric.interfaces.IThreadExecutor;

@Mixin(ThreadExecutor.class)
public class MixinThreadExecutor implements IThreadExecutor {

    @Override
    @Shadow
    public void runTasks() {
    }

}
