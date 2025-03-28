package me.qingshu.essentialinfo.integrations.clothconfigext

import net.minecraft.client.util.InputUtil
import net.minecraft.text.Text

data class MultiKeycode(
    val keys: Set<Int> = emptySet(),
    val isListening: Boolean = false,
) {
    fun toText(): Text =
        Text.literal(
            keys.joinToString(" + ") {
                InputUtil.fromKeyCode(it, 0).localizedText.string
            },
        )
}
