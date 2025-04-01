package me.qingshu.essentialinfo.utils

import me.qingshu.essentialinfo.client.EssentialinfoClient.Companion.mc

object Utils {
    @JvmStatic
    fun canUpdate(): Boolean = mc.world != null && mc.player != null
}
