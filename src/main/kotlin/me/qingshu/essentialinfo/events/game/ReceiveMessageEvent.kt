package me.qingshu.essentialinfo.events.game

import me.qingshu.essentialinfo.events.CancellableEvent
import net.minecraft.client.gui.hud.MessageIndicator
import net.minecraft.text.Text

object ReceiveMessageEvent : CancellableEvent() {
    @JvmField
    var message: Text? = null

    @JvmField
    var indicator: MessageIndicator? = null

    @JvmStatic
    fun get(
        message: Text?,
        indicator: MessageIndicator?,
    ): ReceiveMessageEvent {
        this.message = message
        this.indicator = indicator
        return this
    }
}
