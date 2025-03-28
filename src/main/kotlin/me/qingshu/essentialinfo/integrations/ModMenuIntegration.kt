package me.qingshu.essentialinfo.integrations

import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi
import me.qingshu.essentialinfo.config.ModConfig
import me.qingshu.essentialinfo.config.TransKey.KEY_CONFIG_TITLE
import me.qingshu.essentialinfo.config.TransKey.KEY_DISPLAY_DURATION
import me.qingshu.essentialinfo.config.TransKey.KEY_DISPLAY_DURATION_COMMENT
import me.qingshu.essentialinfo.config.TransKey.KEY_GENERAL
import me.qingshu.essentialinfo.config.TransKey.KEY_HOTKEY
import me.qingshu.essentialinfo.config.TransKey.KEY_OPEN_GUI
import me.qingshu.essentialinfo.config.TransKey.KEY_OPEN_GUI_COMMENT
import me.qingshu.essentialinfo.config.TransKey.KEY_SHOW_DAMAGE_BOSS_BAR
import me.qingshu.essentialinfo.config.TransKey.KEY_SHOW_DAMAGE_BOSS_BAR_COMMENT
import me.qingshu.essentialinfo.integrations.clothconfigext.build
import me.shedaniel.clothconfig2.api.ConfigBuilder
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.util.InputUtil
import net.minecraft.text.Text

class ModMenuIntegration : ModMenuApi {
    companion object {
        private val isClothConfigLoaded: Boolean by lazy {
            FabricLoader.getInstance().isModLoaded("cloth-config")
        }

        @JvmStatic
        fun buildClothConfig(parent: Screen?): Screen? {
            if (!isClothConfigLoaded) return null
            val builder =
                ConfigBuilder
                    .create()
                    .setParentScreen(parent)
                    .setTitle(Text.translatable(KEY_CONFIG_TITLE))
                    .setSavingRunnable {
                        ModConfig.save()
                    }
            generalConfig(builder)
            hotkeyConfig(builder)
            return builder.build()
        }
    }

    override fun getModConfigScreenFactory() =
        ConfigScreenFactory { parent ->
            buildClothConfig(parent)
        }
}

private fun generalConfig(builder: ConfigBuilder) {
    val general = builder.getOrCreateCategory(Text.translatable(KEY_GENERAL))
    val entryBuilder = builder.entryBuilder()

    general.addEntry(
        entryBuilder
            .startBooleanToggle(
                Text.translatable(KEY_SHOW_DAMAGE_BOSS_BAR),
                ModConfig.showDamageBossBar,
            ).build {
                setDefaultValue(true)
                setTooltip(Text.translatable(KEY_SHOW_DAMAGE_BOSS_BAR_COMMENT))
                setSaveConsumer { newValue -> ModConfig.showDamageBossBar = newValue }
            },
    )

    general.addEntry(
        entryBuilder
            .startIntField(
                Text.translatable(KEY_DISPLAY_DURATION),
                ModConfig.displayDuration,
            ).build {
                setDefaultValue(2000)
                setTooltip(Text.translatable(KEY_DISPLAY_DURATION_COMMENT))
                setSaveConsumer { newValue -> ModConfig.displayDuration = newValue }
            },
    )
}

private fun hotkeyConfig(builder: ConfigBuilder) {
    val hotkey = builder.getOrCreateCategory(Text.translatable(KEY_HOTKEY))
    val entryBuilder = builder.entryBuilder()

    hotkey.addEntry(
        entryBuilder
            .startKeyCodeField(
                Text.translatable(KEY_OPEN_GUI),
                InputUtil.Type.KEYSYM.createFromCode(ModConfig.openGUI),
            ).build {
                setDefaultValue(InputUtil.Type.KEYSYM.createFromCode(InputUtil.GLFW_KEY_F4))
                setTooltip(Text.translatable(KEY_OPEN_GUI_COMMENT))
                setKeySaveConsumer { ModConfig.openGUI = it.code }
            },
    )
}
