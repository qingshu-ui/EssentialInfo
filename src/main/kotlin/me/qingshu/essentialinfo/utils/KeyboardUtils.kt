package me.qingshu.essentialinfo.utils

import me.qingshu.essentialinfo.events.ListenerPriority
import me.qingshu.essentialinfo.events.game.KeyEvent
import me.qingshu.essentialinfo.events.on
import org.lwjgl.glfw.GLFW

object KeyboardUtils {
    private val allPressKey = mutableSetOf<Int>()

    fun init() {
        KeyEvent.on(ListenerPriority.HIGHEST) {
            if (event.key != GLFW.GLFW_KEY_UNKNOWN) {
                when (event.action) {
                    KeyEvent.KeyAction.Press -> pressKey(event.key)
                    KeyEvent.KeyAction.Release -> releaseKey(event.key)
                    KeyEvent.KeyAction.Repeat -> {}
                }
            }
        }
    }

    @JvmStatic
    fun pressKey(key: Int) {
        allPressKey.add(key)
    }

    @JvmStatic
    fun releaseKey(key: Int) {
        allPressKey.remove(key)
    }

    @JvmStatic
    fun isKeysPressed(vararg key: Int): Boolean = allPressKey.containsAll(key.toSet()) && allPressKey.size == key.size

    @JvmStatic
    fun isKeyPressed(key: Int): Boolean = allPressKey.contains(key) && allPressKey.size == 1
}
