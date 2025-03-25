package me.qingshu.essentialinfo.mixins.minecraft;

import me.qingshu.essentialinfo.core.DamageTracker;
import me.qingshu.essentialinfo.core.PlayerAttackTracker;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity {

    @Shadow
    public abstract void heal(float amount);

    @Shadow
    public abstract boolean damage(DamageSource source, float amount);

    @Inject(
            method = "setHealth",
            at = @At("HEAD")
    )
    private void onSetHealth(float health, CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        float damage = entity.getHealth() - health;

        if (damage > 0.001f && PlayerAttackTracker.isLastAttacked(entity)) {
            DamageTracker.showDamage(damage, entity);
        }
    }
}
