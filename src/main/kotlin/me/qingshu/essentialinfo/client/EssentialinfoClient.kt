package me.qingshu.essentialinfo.client

import me.qingshu.essentialinfo.Essentialinfo
import me.qingshu.essentialinfo.core.PlayerAttackTracker
import net.fabricmc.api.ClientModInitializer
import net.minecraft.client.MinecraftClient

@Suppress("SpellCheckingInspection")
class EssentialinfoClient : ClientModInitializer {
    companion object {
        private val log get() = Essentialinfo.logger
        var instance: EssentialinfoClient? = null

        @JvmStatic
        val mc: MinecraftClient by lazy { MinecraftClient.getInstance() }
    }

    override fun onInitializeClient() {
        log.info("Mod client initial...")
        if (instance != null) instance = this
        PlayerAttackTracker.init()
    }
}
