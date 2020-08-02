package thallium.fabric.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

@Mixin(LootableContainerBlockEntity.class)
public abstract class MixinLootableContainerBlockEntity {

    @Shadow
    public void checkLootInteraction(PlayerEntity player){}

    @Shadow
    protected abstract DefaultedList<ItemStack> getInvStackList();

    /**
     * @author Paper
     * @reason Optimize isEmpty
     */
    @Overwrite
    public boolean isEmpty() {
        checkLootInteraction(null);
        for (ItemStack itemStack : getInvStackList())
            if (!itemStack.isEmpty())
                return false;
        return true;
    }

    /**
     * @author Paper
     * @reason Optimize getStack
     */
    @Overwrite
    public ItemStack getStack(int slot) {
        if (slot == 0) checkLootInteraction(null); // Paper
        return getInvStackList().get(slot);
    }

}