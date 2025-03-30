package me.qingshu.essentialinfo.mixins.minecraft;

import me.qingshu.essentialinfo.events.EventHandler;
import me.qingshu.essentialinfo.events.render.Render2DEvent;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class MixinInGameHud {

    @Inject(
            method = "render",
            at = @At("TAIL")
    )
    private void onRender(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        EventHandler.emit(Render2DEvent.get(context));
    }
}
