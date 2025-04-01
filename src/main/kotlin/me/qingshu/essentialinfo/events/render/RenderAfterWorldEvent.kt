package me.qingshu.essentialinfo.events.render

import me.qingshu.essentialinfo.events.SimpleEvent
import net.minecraft.client.render.BufferBuilderStorage
import net.minecraft.client.render.Camera
import net.minecraft.client.util.math.MatrixStack
import org.joml.Matrix4f

object RenderAfterWorldEvent : SimpleEvent() {
    @JvmStatic
    lateinit var matrix: MatrixStack

    @JvmStatic
    lateinit var matrix4f: Matrix4f

    @JvmStatic
    lateinit var matrix4f2: Matrix4f

    @JvmStatic
    lateinit var camera: Camera

    @JvmStatic
    lateinit var bufferBuilders: BufferBuilderStorage

    @JvmStatic
    fun get(
        matrix4f: Matrix4f,
        matrix4f2: Matrix4f,
        camera: Camera,
        bufferBuilders: BufferBuilderStorage,
    ): RenderAfterWorldEvent {
        this.matrix4f = matrix4f
        this.matrix4f2 = matrix4f2
        this.camera = camera
        this.bufferBuilders = bufferBuilders
        return this
    }
}
