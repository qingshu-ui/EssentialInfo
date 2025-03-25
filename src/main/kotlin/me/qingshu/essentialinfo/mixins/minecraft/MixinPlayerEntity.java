package me.qingshu.essentialinfo.mixins.minecraft;

import me.qingshu.essentialinfo.core.PlayerAttackTracker;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity {

    @Inject(
            method = "attack",
            at = @At("HEAD")
    )
    private void onAttack(Entity target, CallbackInfo ci) {
        if (target instanceof LivingEntity livingEntity) {
            PlayerAttackTracker.recordAttack(livingEntity);
        }
    }
}
