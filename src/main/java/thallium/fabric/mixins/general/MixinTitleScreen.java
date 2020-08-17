package thallium.fabric.mixins.general;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import thallium.fabric.ThalliumUpdateCheck;

@Mixin(TitleScreen.class)
public class MixinTitleScreen extends Screen {

    protected MixinTitleScreen(Text title) {
        super(title);
    }

    private int tX = 0;

    @Inject(at = { @At(value = "RETURN") }, method = { "render" })
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        ThalliumUpdateCheck.check();
        if (ThalliumUpdateCheck.current.equalsIgnoreCase("${version}"))
            return;
        String txt = "*New* " + ThalliumUpdateCheck.latestFull + " is out!";
        String txt2 = "Please download the latest update on CurseForge!";

        if (tX > this.width + txt.length()*7) tX = -10;
        tX++;

        int l = MathHelper.ceil(1 * 255.0f) << 24;
        super.drawCenteredString(matrices, this.textRenderer, txt, tX, 4, 0xFFFFFF | l);
        super.drawCenteredString(matrices, this.textRenderer, txt2, tX, 12, 0xFFFFFF | l);
    }


}