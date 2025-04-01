package me.qingshu.essentialinfo.core.attack

import me.qingshu.essentialinfo.config.ModConfig
import me.qingshu.essentialinfo.core.attack.EntityStates.PARTICLE
import net.minecraft.entity.LivingEntity
import kotlin.math.min

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
data class EntityState(
    val entity: LivingEntity,
    var health: Float = 0f,
    var previousHealth: Float = 0f,
    var previousHealthDisplay: Float = 0f,
    var previousHealthDelay: Float = 0f,
    var lastDmg: Float = 0f,
    var lastDmgCumulative: Float = 0f,
    var lastHealth: Float = 0f,
    var lastDmgDelay: Float = 0f,
    var animationSpeed: Float = 0f,
) {
    companion object {
        const val HEALTH_INDICATOR_DELAY: Float = 10f
    }

    fun tick() {
        health = min(entity.health, entity.maxHealth)
        incrementTimes()
        if (lastHealth < 0.1) {
            reset()
        } else if (lastHealth != health) {
            handleHealthChange()
        } else if (lastDmgDelay == 0.0f) {
            reset()
        }
        updateAnimations()
    }

    private fun updateAnimations() {
        if (previousHealthDelay > 0) {
            val diff = previousHealthDelay - health
            if (diff > 0) {
                animationSpeed = diff / 10f
            }
        } else if (previousHealthDelay < 1 && previousHealthDisplay > health) {
            previousHealthDisplay -= animationSpeed
        } else {
            previousHealthDisplay = health
            previousHealth = health
            previousHealthDelay = HEALTH_INDICATOR_DELAY
        }
    }

    private fun handleHealthChange() {
        lastDmg = lastHealth - health
        lastDmgCumulative += lastDmg

        lastDmgDelay = HEALTH_INDICATOR_DELAY * 2
        lastHealth = health

        if (ModConfig.attackParticle.enable) {
            PARTICLE.add(AttackParticle(entity, lastDmg))
        }
    }

    private fun reset() {
        lastHealth = health
        lastDmg = 0F
        lastDmgCumulative = 0F
    }

    private fun incrementTimes() {
        if (this.lastDmgDelay > 0) {
            this.lastDmgDelay--
        }
        if (this.previousHealthDelay > 0) {
            this.previousHealthDelay--
        }
    }
}
