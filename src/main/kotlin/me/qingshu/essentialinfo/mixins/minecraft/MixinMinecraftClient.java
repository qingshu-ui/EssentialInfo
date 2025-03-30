package me.qingshu.essentialinfo.mixins.minecraft;

import me.qingshu.essentialinfo.events.EventHandler;
import me.qingshu.essentialinfo.events.world.TickEvent;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MixinMinecraftClient {

    @Inject(
            method = "tick",
            at = @At("HEAD")
    )
    private void onTickPre(CallbackInfo ci) {
        EventHandler.emit(TickEvent.Pre.INSTANCE);
    }

    @Inject(
            method = "tick",
            at = @At("TAIL")
    )
    private void onTickPost(CallbackInfo ci) {
        EventHandler.emit(TickEvent.Post.INSTANCE);
    }
}
