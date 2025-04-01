package me.qingshu.essentialinfo.core

import com.mojang.blaze3d.systems.RenderSystem
import me.qingshu.essentialinfo.Essentialinfo
import me.qingshu.essentialinfo.config.ModConfig
import me.qingshu.essentialinfo.core.attack.AttackParticle
import me.qingshu.essentialinfo.core.attack.EntityStates
import me.qingshu.essentialinfo.events.ListenerPriority
import me.qingshu.essentialinfo.events.entity.player.AttackEntityEvent
import me.qingshu.essentialinfo.events.on
import me.qingshu.essentialinfo.events.render.RenderAfterWorldEvent
import me.qingshu.essentialinfo.events.world.TickEvent
import net.minecraft.client.MinecraftClient
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.render.Camera
import net.minecraft.client.render.GameRenderer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.text.Text
import net.minecraft.util.math.MathHelper.lerp
import net.minecraft.util.math.RotationAxis
import org.joml.Matrix4f
import org.lwjgl.opengl.GL11
import kotlin.math.abs
import kotlin.math.round

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

        TickEvent.Post.on {
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

        RenderAfterWorldEvent.on(ListenerPriority.LOW) {
            if (!ModConfig.attackParticle.enable) return@on
            for (particle in EntityStates.PARTICLE) {
                renderParticle(particle, event.camera, event.vertexConsumers)
            }
        }
        log.info("PlayerAttackTracker initialized.")
    }

    // Copyright (c) 2020 ToroHealth
    // Copyright (c) 2025 lovlellter
    //
    // 此代码基于 ToroHealth (遵循 GPL 3.0 许可证)。
    // 原项目地址: https://github.com/ToroCraft/ToroHealth
    // 原许可证: https://github.com/ToroCraft/ToroHealth/blob/master/LICENSE
    //
    // 修改内容:
    // - Java to Kotlin
    // - Update api call
    private fun renderParticle(
        particle: AttackParticle,
        camera: Camera,
        vertexConsumers: VertexConsumerProvider,
    ) {
        val distanceSquared = camera.pos.squaredDistanceTo(particle.x, particle.y, particle.z)
        if (distanceSquared > ModConfig.attackParticle.distanceSquared) {
            return
        }

        val scaleToGui = 0.025f
        val tickDelta = mc.renderTickCounter.getTickDelta(false).toDouble()

        val x = lerp(tickDelta, particle.xPrev, particle.x)
        val y = lerp(tickDelta, particle.yPrev, particle.y)
        val z = lerp(tickDelta, particle.zPrev, particle.z)

        val camPos = camera.pos
        val camX = camPos.x
        val camY = camPos.y
        val camZ = camPos.z

        val modelMatrix =
            Matrix4f().apply {
                translate(
                    (x - camX).toFloat(),
                    (y - camY).toFloat(),
                    (z - camZ).toFloat(),
                )

                rotate(RotationAxis.POSITIVE_Y.rotationDegrees(-camera.yaw))
                rotate(RotationAxis.POSITIVE_X.rotationDegrees(camera.pitch))

                scale(-scaleToGui, -scaleToGui, scaleToGui)
            }

        RenderSystem.setShader(GameRenderer::getPositionColorProgram)
        RenderSystem.enableDepthTest()
        RenderSystem.enableBlend()
        RenderSystem.blendFuncSeparate(
            GL11.GL_SRC_ALPHA,
            GL11.GL_ONE_MINUS_SRC_ALPHA,
            GL11.GL_ONE,
            GL11.GL_ZERO,
        )

        drawDamageNumber(
            modelMatrix,
            vertexConsumers,
            particle.damage,
        )
        RenderSystem.disableBlend()
    }

    // Copyright (c) 2020 ToroHealth
    // Copyright (c) 2025 lovlellter
    //
    // 此代码基于 ToroHealth (遵循 GPL 3.0 许可证)。
    // 原项目地址: https://github.com/ToroCraft/ToroHealth
    // 原许可证: https://github.com/ToroCraft/ToroHealth/blob/master/LICENSE
    //
    // 修改内容:
    // - Java to Kotlin
    // - Update api call
    private fun drawDamageNumber(
        matrix: Matrix4f,
        vertexConsumers: VertexConsumerProvider,
        damage: Float,
        x: Double = 0.0,
        y: Double = 0.0,
        width: Double = 10.0,
        light: Int = 15,
    ) {
        if (abs(round(damage)) == 0f) return
        val dmgText = Text.literal(if (damage < 0) "+ %.1f".format(-damage) else "- %.1f".format(damage))
        val color = if (damage < 0) ModConfig.attackParticle.healColor else ModConfig.attackParticle.damageColor
        val sw = mc.textRenderer.getWidth(dmgText)
        mc.textRenderer.draw(
            dmgText,
            (x + (width / 2) - sw).toFloat(),
            (y + 5).toFloat(),
            color,
            false,
            matrix,
            vertexConsumers,
            TextRenderer.TextLayerType.SEE_THROUGH,
            0,
            light,
        )
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
        return entity.id == lastAttackedEntity &&
            System.currentTimeMillis() - lastAttackTime < 1000
    }

    @JvmStatic
    fun getLastHealth() = lastHealth
}
