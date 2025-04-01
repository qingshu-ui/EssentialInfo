package me.qingshu.essentialinfo.events.render

import me.qingshu.essentialinfo.events.SimpleEvent
import net.minecraft.client.render.Camera
import net.minecraft.client.render.VertexConsumerProvider
import org.joml.Matrix4f

object RenderAfterWorldEvent : SimpleEvent() {
    @JvmStatic
    lateinit var matrix: Matrix4f

    @JvmStatic
    lateinit var camera: Camera

    @JvmStatic
    lateinit var vertexConsumers: VertexConsumerProvider.Immediate

    @JvmStatic
    fun get(
        matrix: Matrix4f,
        camera: Camera,
        vertexConsumers: VertexConsumerProvider.Immediate,
    ): RenderAfterWorldEvent {
        this.matrix = matrix
        this.camera = camera
        this.vertexConsumers = vertexConsumers
        return this
    }
}
