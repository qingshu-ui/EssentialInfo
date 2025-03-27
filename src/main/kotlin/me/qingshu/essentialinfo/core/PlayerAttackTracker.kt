package me.qingshu.essentialinfo.core

import me.qingshu.essentialinfo.Essentialinfo
import me.qingshu.essentialinfo.config.ModConfig
import me.qingshu.essentialinfo.events.entity.player.AttackEntityEvent
import me.qingshu.essentialinfo.events.on
import me.qingshu.essentialinfo.events.world.TickEvent
import net.minecraft.client.MinecraftClient
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity

object PlayerAttackTracker {
    private var lastAttackedEntity: Int = -1
    private var lastAttackTime: Long = -1
    private var lastHealth: Float = 0f
    private val mc = MinecraftClient.getInstance()
    private val log = Essentialinfo.logger

    fun init() {
        AttackEntityEvent.on {
            val entity: Entity = event.entity ?: return@on
            if (entity is LivingEntity) {
                recordAttack(entity)
            }
        }

        TickEvent.on {
            if (!ModConfig.showDamageBossBar) return@on
            val world = mc.world ?: return@on
            val entity = world.getEntityById(lastAttackedEntity) as? LivingEntity
            if (entity != null && isLastAttacked(entity)) {
                val damage = getLastHealth() - entity.health
                if (damage > 0f) {
                    DamageTracker.showDamage(damage, entity)
                }
            }
        }
        log.info("PlayerAttackTracker initialized.")
    }

    @JvmStatic
    fun recordAttack(entity: LivingEntity) {
        lastAttackedEntity = entity.id
        lastAttackTime = System.currentTimeMillis()
        lastHealth = entity.health
    }

    @JvmStatic
    fun isLastAttacked(entity: LivingEntity): Boolean {
        if (lastAttackedEntity == -1) return false
        return entity.id == lastAttackedEntity
               && System.currentTimeMillis() - lastAttackTime < 1000
    }

    @JvmStatic
    fun getLastHealth() = lastHealth
}