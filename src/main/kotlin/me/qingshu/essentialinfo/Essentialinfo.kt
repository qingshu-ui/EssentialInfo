package me.qingshu.essentialinfo

import net.fabricmc.api.ModInitializer
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Suppress("SpellCheckingInspection")
class Essentialinfo : ModInitializer {
    companion object {
        private const val MOD_ID: String = "essentialinfo"

        @JvmStatic
        var instance: Essentialinfo? = null

        @JvmStatic
        val logger: Logger by lazy {
            LoggerFactory.getLogger(MOD_ID)
        }
    }

    override fun onInitialize() {
        if (instance == null) instance = this
        logger.info("Start...")
    }
}
