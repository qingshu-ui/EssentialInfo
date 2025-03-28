package me.qingshu.essentialinfo.integrations.clothconfigext

import me.qingshu.essentialinfo.client.EssentialinfoClient.Companion.mc
import me.shedaniel.clothconfig2.gui.entries.TooltipListEntry
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.Element
import net.minecraft.client.gui.Selectable
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.gui.widget.ClickableWidget
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import java.util.*
import java.util.function.Supplier

@Suppress("UnstableApiUsage", "DEPRECATION")
class MultiKeycodeEntry(
    private val fieldName: Text,
    private val tooltipSupplier: Supplier<Optional<Array<Text>>>,
    private val requiresRestart: Boolean,

    private var value: MultiKeycode,
    val original: MultiKeycode,
    val saveCallback: (MultiKeycode) -> Unit,
    private val resetBtnKey: Text,
    private val defaultValue: Supplier<MultiKeycode>,
) : TooltipListEntry<MultiKeycode>(
    fieldName, tooltipSupplier, requiresRestart
) {
    private val btnWidget: ButtonWidget = ButtonWidget.builder(Text.empty(), ::onPress)
        .dimensions(0, 0, 150, 20)
        .build()

    private val resetBtn: ButtonWidget = ButtonWidget.builder(resetBtnKey, ::onResetPress)
        .dimensions(0, 0, mc.textRenderer.getWidth(resetBtnKey) + 6, 20)
        .build()

    private val widgets: List<ClickableWidget> = mutableListOf(btnWidget, resetBtn)

    private fun onResetPress(buttonWidget: ButtonWidget?) {
        value = defaultValue.get()
    }

    private fun onPress(buttonWidget: ButtonWidget?) {
        TODO("Not yet implemented")
    }

    override fun narratables(): MutableList<out Selectable> {
        return widgets as MutableList<out Selectable>
    }

    override fun children(): MutableList<out Element> {
        return widgets as MutableList<out Element>
    }

    override fun getValue(): MultiKeycode {
        return value
    }

    override fun getDefaultValue(): Optional<MultiKeycode> {
        return Optional.ofNullable(defaultValue).map { it.get() }
    }

    private fun getLocalizedName(): Text {
        return value.toText()
    }

    override fun render(
        graphics: DrawContext?,
        index: Int,
        y: Int,
        x: Int,
        entryWidth: Int,
        entryHeight: Int,
        mouseX: Int,
        mouseY: Int,
        isHovered: Boolean,
        delta: Float
    ) {
        super.render(graphics, index, y, x, entryWidth, entryHeight, mouseX, mouseY, isHovered, delta)
        val window = mc.window
        resetBtn.active = isEditable && getDefaultValue().isPresent && defaultValue.get() != getValue()
        resetBtn.y = y
        btnWidget.active = isEditable
        btnWidget.y = y
        btnWidget.message = getLocalizedName()
        if (btnWidget.isFocused) {
            btnWidget.message = Text.literal("> ").formatted(Formatting.WHITE)
                .append(btnWidget.message.copyContentOnly().formatted(Formatting.YELLOW))
                .append(Text.literal(" <").formatted(Formatting.WHITE))
        }

        if (mc.textRenderer.isRightToLeft) {
            graphics?.drawTextWithShadow(
                mc.textRenderer,
                displayedFieldName.asOrderedText(),
                window.scaledWidth - x - mc.textRenderer.getWidth(displayedFieldName),
                y + 6,
                16777215
            )
            resetBtn.x = x
            btnWidget.x = x + resetBtn.width + 2
        } else {
            graphics?.drawTextWithShadow(
                mc.textRenderer,
                displayedFieldName.asOrderedText(),
                x,
                y + 6,
                preferredTextColor
            )
            resetBtn.x = x + entryWidth - resetBtn.width
            btnWidget.x = x + entryWidth - 150
        }
        btnWidget.width = 150 - resetBtn.width - 2
        resetBtn.render(graphics, mouseX, mouseY, delta)
        btnWidget.render(graphics, mouseX, mouseY, delta)
    }

}