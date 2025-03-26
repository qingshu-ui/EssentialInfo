package me.qingshu.essentialinfo.events

interface Event {
    val cancel: Boolean
    val cancellable: Boolean
}

open class CancellableEvent : Event {
    override var cancel: Boolean = false
    override val cancellable: Boolean = true
}

open class SimpleEvent : Event {
    override val cancel: Boolean = false
    override val cancellable: Boolean = false
}