package me.qingshu.essentialinfo.core

import me.qingshu.essentialinfo.config.ModConfig
import me.qingshu.essentialinfo.mixininterface.IBossBarHud
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.hud.ClientBossBar
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.boss.BossBar
import net.minecraft.text.Text
import java.util.*

object DamageTracker {
    private var lastShowTime: Long = 0
    private val bossBar by lazy {
        ClientBossBar(
            UUID.randomUUID(),
            Text.literal(""),
            1.0f,
            BossBar.Color.RED,
            BossBar.Style.PROGRESS,
            false,
            false,
            false
        )
    }

    @JvmStatic
    fun showDamage(damage: Float, target: LivingEntity) {
        val healthPercent = (target.health - damage).coerceAtLeast(0f) / target.maxHealth
        bossBar.apply {
            name = Text.literal(String.format("- %.1f", damage))
            percent = healthPercent.coerceIn(0f, 1f)
        }
        val bossBarHud = MinecraftClient.getInstance().inGameHud.bossBarHud as IBossBarHud
        bossBarHud.`essentialInfo$addBossBar`(bossBar)
        lastShowTime = System.currentTimeMillis()
    }

    @JvmStatic
    fun tick() {
        if (System.currentTimeMillis() - lastShowTime > ModConfig.displayDuration) {
            val bossBarHud = MinecraftClient.getInstance().inGameHud.bossBarHud as IBossBarHud
            bossBarHud.`essentialInfo$removeBossBar`(bossBar)
        }
    }
}
