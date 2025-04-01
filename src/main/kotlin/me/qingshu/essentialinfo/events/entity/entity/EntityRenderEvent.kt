package me.qingshu.essentialinfo.events.entity.entity

import me.qingshu.essentialinfo.events.SimpleEvent
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.Entity

object EntityRenderEvent : SimpleEvent() {
    @JvmStatic
    lateinit var matrixStack: MatrixStack

    @JvmStatic
    lateinit var entity: Entity

    @JvmStatic
    fun get(
        matrixStack: MatrixStack,
        entity: Entity,
    ): EntityRenderEvent {
        this.matrixStack = matrixStack
        this.entity = entity
        return this
    }
}
