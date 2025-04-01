package me.qingshu.essentialinfo.core.attack

import me.qingshu.essentialinfo.Essentialinfo.Companion.RAND
import me.qingshu.essentialinfo.client.EssentialinfoClient.Companion.mc
import net.minecraft.entity.Entity

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
 * - class rename
 */
data class AttackParticle(
    var x: Double = 0.0,
    var y: Double = 0.0,
    var z: Double = 0.0,
    var xPrev: Double = 0.0,
    var yPrev: Double = 0.0,
    var zPrev: Double = 0.0,
    var age: Int = 0,
    val ax: Double = 0.00,
    val ay: Double = -0.01,
    val az: Double = 0.00,
    var vx: Double = 0.0,
    var vy: Double = 0.0,
    var vz: Double = 0.0,
) {
    var damage: Float = 0.0f

    constructor(entity: Entity, damage: Float) : this() {
        val entityLocation = entity.pos.add(0.0, entity.height / 2.0, 0.0)
        val cameraLocation = mc.gameRenderer.camera.pos
        val offsetBy = entity.width.toDouble()
        val offset = cameraLocation.subtract(entityLocation).normalize().multiply(offsetBy)
        val pos = entityLocation.add(offset)

        age = 0
        this.damage = damage

        vx = RAND.nextDouble() * 0.04
        vy = 0.10 + (RAND.nextDouble() * 0.05)
        vz = RAND.nextDouble() * 0.04

        x = pos.x
        y = pos.y
        z = pos.z

        xPrev = x
        yPrev = y
        zPrev = z
    }

    fun tick() {
        xPrev = x
        yPrev = y
        zPrev = z
        age++
        x += vx
        y += vy
        z += vz
        vx += ax
        vy += ay
        vz += az
    }
}
