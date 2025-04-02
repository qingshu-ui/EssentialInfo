package me.qingshu.essentialinfo.config

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.util.InputUtil
import java.nio.file.Files
import java.nio.file.Path

@Serializable
data class AttackParticle(
    var enable: Boolean = true,
    var damageColor: Int = 0xff0000,
    var healColor: Int = 0x00ff00,
    var distance: Int = 60,
) {
    val distanceSquared
        get() = distance * distance
}

@Serializable
data class ModConfigData(
    var showDamageBossBar: Boolean = true,
    var displayDuration: Int = 2000,
    var openGUI: Int = InputUtil.GLFW_KEY_F4,
    var attackParticle: AttackParticle = AttackParticle(),
) {
    companion object {
        val configPath: Path
            get() = FabricLoader.getInstance().configDir.resolve("essentialinfo.json")
        val json =
            Json {
                prettyPrint = true
                ignoreUnknownKeys = true
                encodeDefaults = true
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

    @JvmStatic
    var attackParticle by data::attackParticle

    fun save() = data.save()
}

object TransKey {
    private const val CONFIG_PREFIX = "config.essentialinfo"

    // general
    const val KEY_GENERAL: String = "${CONFIG_PREFIX}.general"
    const val KEY_CONFIG_TITLE = "${CONFIG_PREFIX}.title"
    const val KEY_SHOW_DAMAGE_BOSS_BAR: String = "${CONFIG_PREFIX}.showDamageBossBar"
    const val KEY_SHOW_DAMAGE_BOSS_BAR_COMMENT: String = "$KEY_SHOW_DAMAGE_BOSS_BAR.comment"
    const val KEY_DISPLAY_DURATION: String = "${CONFIG_PREFIX}.displayDuration"
    const val KEY_DISPLAY_DURATION_COMMENT: String = "$KEY_DISPLAY_DURATION.comment"

    // attack particle
    const val KEY_ATTACK_PARTICLE: String = "$CONFIG_PREFIX.attackParticle"
    const val KEY_ATTACK_PARTICLE_ENABLE: String = "$KEY_ATTACK_PARTICLE.enable"
    const val KEY_ATTACK_PARTICLE_ENABLE_COMMENT: String = "$KEY_ATTACK_PARTICLE_ENABLE.comment"
    const val KEY_ATTACK_PARTICLE_DAMAGE_COLOR: String = "$KEY_ATTACK_PARTICLE.damageColor"
    const val KEY_ATTACK_PARTICLE_DAMAGE_COLOR_COMMENT: String = "$KEY_ATTACK_PARTICLE_DAMAGE_COLOR.comment"
    const val KEY_ATTACK_PARTICLE_HEAL_COLOR: String = "$KEY_ATTACK_PARTICLE.healColor"
    const val KEY_ATTACK_PARTICLE_HEAL_COLOR_COMMENT: String = "$KEY_ATTACK_PARTICLE_HEAL_COLOR.comment"
    const val KEY_ATTACK_PARTICLE_DISTANCE: String = "$KEY_ATTACK_PARTICLE.distance"
    const val KEY_ATTACK_PARTICLE_DISTANCE_COMMENT: String = "$KEY_ATTACK_PARTICLE_DISTANCE.comment"

    // hotkey
    const val KEY_HOTKEY: String = "${CONFIG_PREFIX}.hotkey"
    const val KEY_OPEN_GUI: String = "${CONFIG_PREFIX}.hotkey.opengui"
    const val KEY_OPEN_GUI_COMMENT: String = "${CONFIG_PREFIX}.hotkey.opengui.comment"
}
