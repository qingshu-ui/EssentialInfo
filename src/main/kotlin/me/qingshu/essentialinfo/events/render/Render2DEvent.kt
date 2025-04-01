package me.qingshu.essentialinfo.events.render

import me.qingshu.essentialinfo.events.SimpleEvent
import net.minecraft.client.gui.DrawContext

object Render2DEvent : SimpleEvent() {
    @JvmStatic
    var drawContext: DrawContext? = null

    @JvmStatic
    fun get(drawContext: DrawContext): Render2DEvent {
        this.drawContext = drawContext
        return this
    }
}
