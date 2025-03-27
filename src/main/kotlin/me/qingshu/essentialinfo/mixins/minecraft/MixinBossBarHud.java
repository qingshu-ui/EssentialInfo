package me.qingshu.essentialinfo.mixins.minecraft;

import me.qingshu.essentialinfo.mixininterface.IBossBarHud;
import net.minecraft.client.gui.hud.BossBarHud;
import net.minecraft.client.gui.hud.ClientBossBar;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;
import java.util.UUID;

@Mixin(BossBarHud.class)
public abstract class MixinBossBarHud implements IBossBarHud {

    @Shadow
    @Final
    Map<UUID, ClientBossBar> bossBars;

    @Override
    public void essentialInfo$addBossBar(@NotNull ClientBossBar bossBar) {
        this.bossBars.put(bossBar.getUuid(), bossBar);
    }

    @Override
    public void essentialInfo$removeBossBar(@NotNull ClientBossBar bossBar) {
        this.bossBars.remove(bossBar.getUuid());
    }
}
