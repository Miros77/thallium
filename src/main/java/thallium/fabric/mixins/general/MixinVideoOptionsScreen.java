package thallium.fabric.mixins.general;

import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.screen.VideoOptionsScreen;
import net.minecraft.client.gui.screen.options.GameOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget.PressAction;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import thallium.fabric.gui.ThalliumOptionsScreen;

@Mixin(VideoOptionsScreen.class)
public class MixinVideoOptionsScreen extends GameOptionsScreen {

    protected MixinVideoOptionsScreen(Text title) {
        super(null, null, title);
    }

    @Inject(at = { @At(value = "RETURN") }, method = { "init" })
    public void addCustomButton(CallbackInfo ci) {
        this.addButton(new ButtonWidget(this.width / 2 + 4, this.height - 27, 100, 20, new LiteralText("Thallium Options"), button -> {
            this.client.openScreen(new ThalliumOptionsScreen(this));
        }));
    }

    @Dynamic
    @Redirect(method = "init", at = @At(value = "NEW", target = "net/minecraft/client/gui/widget/ButtonWidget"))
    private ButtonWidget redirectChunkManager(int x, int y, int width, int height, Text message, PressAction onPress) {
        // Change size of "Done" button to fit ours in.
        return new ButtonWidget(this.width / 2 - 100, this.height - 27, 100, 20, ScreenTexts.DONE, button -> {
            this.client.options.write();
            this.client.getWindow().applyVideoMode();
            this.client.openScreen(this.parent);
        });
    }


}