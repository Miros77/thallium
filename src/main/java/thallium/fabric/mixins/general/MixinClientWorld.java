package thallium.fabric.mixins.general;

import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.world.GameMode;

@Mixin(ClientWorld.class)
public class MixinClientWorld {

    @Shadow public MinecraftClient client;

    // Why does Mojang keep making new instances of Random for this one method?!?
    public Random thalliumRandom = new Random();

    @Overwrite
    public void doRandomBlockDisplayTicks(int xCenter, int yCenter, int zCenter) {
        boolean bl = false;
        if (this.client.interactionManager.getCurrentGameMode() == GameMode.CREATIVE) {
            for (ItemStack itemStack : this.client.player.getItemsHand()) {
                if (itemStack.getItem() != Blocks.BARRIER.asItem()) continue;
                bl = true;
                break;
            }
        }
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        boolean bl2 = bl;

        for (int j = 0; j < 667; ++j) {
            this.randomBlockDisplayTick(xCenter, yCenter, zCenter, 16, thalliumRandom, bl2, mutable);
            this.randomBlockDisplayTick(xCenter, yCenter, zCenter, 32, thalliumRandom, bl2, mutable);
        }
    }


    @Shadow
    public void randomBlockDisplayTick(int xCenter, int yCenter, int zCenter, int i, Random random, boolean bl, Mutable mutable) {
    }

}