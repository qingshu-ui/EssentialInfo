package me.qingshu.essentialinfo.integrations

import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi
import me.qingshu.essentialinfo.config.ModConfig
import me.qingshu.essentialinfo.config.TransKey.KEY_CONFIG_TITLE
import me.qingshu.essentialinfo.config.TransKey.KEY_DISPLAY_DURATION
import me.qingshu.essentialinfo.config.TransKey.KEY_DISPLAY_DURATION_COMMENT
import me.qingshu.essentialinfo.config.TransKey.KEY_GENERAL
import me.qingshu.essentialinfo.config.TransKey.KEY_SHOW_DAMAGE_BOSS_BAR
import me.qingshu.essentialinfo.config.TransKey.KEY_SHOW_DAMAGE_BOSS_BAR_COMMENT
import me.shedaniel.clothconfig2.api.ConfigBuilder
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text

class ModMenuIntegration : ModMenuApi {
    private val isClothConfigLoaded: Boolean by lazy {
        FabricLoader.getInstance().isModLoaded("cloth-config")
    }

    override fun getModConfigScreenFactory() =
        ConfigScreenFactory { parent ->
            buildClothConfig(parent)
        }

    private fun buildClothConfig(parent: Screen): Screen? {
        if (!isClothConfigLoaded) return null
        val builder =
            ConfigBuilder
                .create()
                .setParentScreen(parent)
                .setTitle(Text.translatable(KEY_CONFIG_TITLE))
                .setSavingRunnable {
                    ModConfig.save()
                }
        val entryBuilder = builder.entryBuilder()

        val general = builder.getOrCreateCategory(Text.translatable(KEY_GENERAL))
        general.addEntry(
            entryBuilder
                .startBooleanToggle(
                    Text.translatable(KEY_SHOW_DAMAGE_BOSS_BAR),
                    ModConfig.showDamageBossBar,
                ).setDefaultValue(true)
                .setTooltip(Text.translatable(KEY_SHOW_DAMAGE_BOSS_BAR_COMMENT))
                .setSaveConsumer { newValue -> ModConfig.showDamageBossBar = newValue }
                .build(),
        )

        general.addEntry(
            entryBuilder
                .startIntField(
                    Text.translatable(KEY_DISPLAY_DURATION),
                    ModConfig.displayDuration,
                ).setDefaultValue(2000)
                .setTooltip(Text.translatable(KEY_DISPLAY_DURATION_COMMENT))
                .setSaveConsumer { newValue -> ModConfig.displayDuration = newValue }
                .build(),
        )

        return builder.build()
    }
}
