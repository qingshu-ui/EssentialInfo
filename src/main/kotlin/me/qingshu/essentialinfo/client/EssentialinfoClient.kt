package me.qingshu.essentialinfo.client

import me.qingshu.essentialinfo.Essentialinfo
import net.fabricmc.api.ClientModInitializer

@Suppress("SpellCheckingInspection")
class EssentialinfoClient : ClientModInitializer {
    companion object {
        private val log get() = Essentialinfo.logger
        var instance: EssentialinfoClient? = null
    }

    override fun onInitializeClient() {
        log.info("Mod client initial...")
        if (instance != null) instance = this
    }
}
