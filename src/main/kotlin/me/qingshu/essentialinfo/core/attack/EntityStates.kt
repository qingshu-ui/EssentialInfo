package me.qingshu.essentialinfo.core.attack

import me.qingshu.essentialinfo.client.EssentialinfoClient.Companion.mc
import me.qingshu.essentialinfo.events.entity.entity.EntityRenderEvent
import me.qingshu.essentialinfo.events.on
import me.qingshu.essentialinfo.events.world.TickEvent
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.decoration.ArmorStandEntity
import net.minecraft.entity.mob.CreeperEntity

/**
 * Copyright (c) 2020 ToroHealth
 * Copyright (c) 2025 lovlellter
 *
 * 此代码基于 ToroHealth (遵循 GPL 3.0 许可证)。
 * 原项目地址: https://github.com/ToroCraft/ToroHealth
 * 原许可证: https://github.com/ToroCraft/ToroHealth/blob/master/LICENSE
 *
 * 修改内容:
 * - Java to Kotlin
 * - Class rename
 */
object EntityStates {
    init {
        EntityRenderEvent.on {
            val entity = event.entity as? LivingEntity ?: return@on
            if (!shouldDisplay(entity)) return@on
            if (entity.distanceTo(mc.cameraEntity) > 60f) return@on
            getState(entity)
        }
        TickEvent.Pre.on { tick() }
    }

    private fun shouldDisplay(entity: LivingEntity): Boolean =
        entity !is ArmorStandEntity &&
            !entity.isInvisibleTo(mc.player) ||
            entity.isGlowing ||
            entity.isOnFire ||
            entity is CreeperEntity &&
            entity.shouldRenderOverlay() ||
            entity.equippedItems.any { !it.isEmpty } &&
            entity != mc.player &&
            !entity.isSpectator

    val PARTICLE = ArrayList<AttackParticle>()
    private val STATES: HashMap<Int, EntityState> = hashMapOf()
    private var tickCount = 0

    private fun getState(entity: LivingEntity): EntityState {
        val id = entity.id
        return STATES.getOrPut(id) { EntityState(entity) }
    }

    private fun tick() {
        STATES.values.forEach(EntityState::tick)

        if (tickCount % 200 == 0) {
            cleanCache()
        }
        PARTICLE.forEach(AttackParticle::tick)
        PARTICLE.removeIf { it.age > 50 }

        tickCount++
    }

    private fun cleanCache() {
        STATES.entries.removeIf(::stateExpired)
    }

    private fun stateExpired(entry: MutableMap.MutableEntry<Int, EntityState>): Boolean {
        val world = mc.world ?: return true
        val entity = world.getEntityById(entry.key) as? LivingEntity ?: return true

        if (!world.isChunkLoaded(entity.blockPos.x, entity.blockPos.y)) return true

        return !entity.isAlive
    }
}
