package me.qingshu.essentialinfo.config

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.util.InputUtil
import java.nio.file.Files
import java.nio.file.Path

@Serializable
data class ModConfigData(
    var showDamageBossBar: Boolean = true,
    var displayDuration: Int = 2000,
    var openGUI: Int = InputUtil.GLFW_KEY_F4,
) {
    companion object {
        val configPath: Path
            get() = FabricLoader.getInstance().configDir.resolve("essentialinfo.json")
        val json =
            Json {
                prettyPrint = true
                ignoreUnknownKeys = true
            }

        fun load(): ModConfigData {
            return try {
                if (!Files.exists(configPath)) return ModConfigData()
                json.decodeFromString(Files.readString(configPath))
            } catch (e: Exception) {
                ModConfigData().also { it.save() }
            }
        }
    }

    fun save() {
        Files.writeString(configPath, json.encodeToString(this))
    }
}

object ModConfig {
    private var data = ModConfigData.load()

    @JvmStatic
    var showDamageBossBar by data::showDamageBossBar

    @JvmStatic
    var displayDuration: Int by data::displayDuration

    @JvmStatic
    var openGUI: Int by data::openGUI

    fun save() = data.save()
}

object TransKey {
    private const val CONFIG_PREFIX = "config.essentialinfo"

    const val KEY_GENERAL: String = "${CONFIG_PREFIX}.general"
    const val KEY_CONFIG_TITLE = "${CONFIG_PREFIX}.title"
    const val KEY_SHOW_DAMAGE_BOSS_BAR: String = "${CONFIG_PREFIX}.showDamageBossBar"
    const val KEY_SHOW_DAMAGE_BOSS_BAR_COMMENT: String = "${CONFIG_PREFIX}.showDamageBossBar.comment"
    const val KEY_DISPLAY_DURATION: String = "${CONFIG_PREFIX}.displayDuration"
    const val KEY_DISPLAY_DURATION_COMMENT: String = "${CONFIG_PREFIX}.displayDuration.comment"

    const val KEY_HOTKEY: String = "${CONFIG_PREFIX}.hotkey"
    const val KEY_OPEN_GUI: String = "${CONFIG_PREFIX}.hotkey.opengui"
    const val KEY_OPEN_GUI_COMMENT: String = "${CONFIG_PREFIX}.hotkey.opengui.comment"
}
