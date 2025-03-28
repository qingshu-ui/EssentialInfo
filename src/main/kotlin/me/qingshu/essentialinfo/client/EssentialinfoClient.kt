package me.qingshu.essentialinfo.client

import me.qingshu.essentialinfo.Essentialinfo
import me.qingshu.essentialinfo.config.ModConfig
import me.qingshu.essentialinfo.core.PlayerAttackTracker
import me.qingshu.essentialinfo.events.game.KeyEvent
import me.qingshu.essentialinfo.events.on
import me.qingshu.essentialinfo.integrations.ModMenuIntegration.Companion.buildClothConfig
import me.qingshu.essentialinfo.utils.KeyboardUtils
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
        KeyboardUtils.init()

        KeyEvent.on {
            if (ModConfig.openGUI != -1 &&
                KeyboardUtils.isKeyPressed(ModConfig.openGUI) &&
                mc.currentScreen == null
            ) {
                mc.setScreen(buildClothConfig(mc.currentScreen))
            }
        }
    }
}
