package me.qingshu.essentialinfo.events.game

import me.qingshu.essentialinfo.events.CancellableEvent
import org.lwjgl.glfw.GLFW

object KeyEvent : CancellableEvent() {
    @JvmField
    var key: Int = 0

    @JvmField
    var modifiers: Int = 0

    @JvmField
    var action: KeyAction = KeyAction.Repeat

    @JvmStatic
    fun get(
        key: Int,
        modifiers: Int,
        action: KeyAction,
    ): KeyEvent {
        this.key = key
        this.modifiers = modifiers
        this.action = action
        return this
    }

    enum class KeyAction {
        Press,
        Repeat,
        Release,
        ;

        companion object {
            @JvmStatic
            fun get(action: Int): KeyAction =
                when (action) {
                    GLFW.GLFW_RELEASE -> Release
                    GLFW.GLFW_PRESS -> Press
                    else -> Repeat
                }
        }
    }
}
