package me.qingshu.essentialinfo.mixins.minecraft;

import me.qingshu.essentialinfo.config.ModConfig;
import me.qingshu.essentialinfo.core.DamageTracker;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity {


    @Inject(
            method = "damage",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/LivingEntity;applyDamage(Lnet/minecraft/entity/damage/DamageSource;F)V",
                    shift = At.Shift.AFTER
            )
    )
    private void onDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (!ModConfig.getShowDamageBossBar()) return;
        Entity attacker = source.getAttacker();
        ClientPlayerEntity clientPlayerEntity = MinecraftClient.getInstance().player;
        if (attacker != null && clientPlayerEntity != null
            && attacker.getUuid().equals(clientPlayerEntity.getUuid())) {
            LivingEntity target = (LivingEntity) (Object) this;
            DamageTracker.showDamage(amount, target);
        }
    }
}
