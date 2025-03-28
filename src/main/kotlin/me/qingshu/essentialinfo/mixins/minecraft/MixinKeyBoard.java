
package me.qingshu.essentialinfo.mixins.minecraft;

import me.qingshu.essentialinfo.events.EventHandler;
import me.qingshu.essentialinfo.events.game.KeyEvent;
import net.minecraft.client.Keyboard;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public abstract class MixinKeyBoard {

    @Inject(
            method = "onKey",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onKey(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
        if (key != GLFW.GLFW_KEY_UNKNOWN) {
            boolean isCancelled = EventHandler.emitAndCheckCancel(KeyEvent.get(key, modifiers, KeyEvent.KeyAction.get(action)));
            if (isCancelled) {
                ci.cancel();
            }
        }
    }
}
