package me.qingshu.essentialinfo.mixininterface

import net.minecraft.client.gui.hud.ClientBossBar

interface IBossBarHud {
    @Suppress("FunctionName")
    fun `essentialInfo$addBossBar`(bossBar: ClientBossBar)

    @Suppress("FunctionName")
    fun `essentialInfo$removeBossBar`(bossBar: ClientBossBar)
}
