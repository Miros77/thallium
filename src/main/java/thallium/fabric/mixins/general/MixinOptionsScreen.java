package thallium.fabric.mixins.general;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.options.OptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import thallium.fabric.gui.ThalliumOptionsScreen;

@Mixin(OptionsScreen.class)
public class MixinOptionsScreen extends Screen {

    protected MixinOptionsScreen(Text title) {
        super(title);
    }

    @Inject(at = @At("RETURN"), method = "init")
    public void addCustomButton(CallbackInfo ci) {
        this.addButton(new ButtonWidget(this.width / 2 - 155, this.height / 6 + 144 - 6, 150, 20, new LiteralText("Thallium Options"), button -> {
            this.client.openScreen(new ThalliumOptionsScreen(this));
        }));
    }


}