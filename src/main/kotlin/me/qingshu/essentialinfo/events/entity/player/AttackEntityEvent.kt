package me.qingshu.essentialinfo.events.entity.player

import me.qingshu.essentialinfo.events.CancellableEvent
import me.qingshu.essentialinfo.events.Event
import net.minecraft.entity.Entity

object AttackEntityEvent : CancellableEvent() {
    var entity: Entity? = null

    @JvmStatic
    fun get(target: Entity): Event {
        entity = target
        return AttackEntityEvent
    }
}