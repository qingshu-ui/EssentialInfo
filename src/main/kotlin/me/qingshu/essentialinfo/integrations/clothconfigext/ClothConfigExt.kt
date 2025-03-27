package me.qingshu.essentialinfo.integrations.clothconfigext

import me.shedaniel.clothconfig2.api.AbstractConfigListEntry
import me.shedaniel.clothconfig2.impl.builders.FieldBuilder

class ClothConfigExt


fun <T, A : AbstractConfigListEntry<T>, SELF : FieldBuilder<T, A, SELF>> SELF.build(block: SELF.() -> Unit): A {
    block()
    return this.build()
}