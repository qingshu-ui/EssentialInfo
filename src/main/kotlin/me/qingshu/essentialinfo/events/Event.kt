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

inline fun <reified T : Event> T.on(
    priority: ListenerPriority = ListenerPriority.NORMAL, noinline action: EventHandlerScope<T>.() -> Unit
): ListenerWrapper {
    return EventHandler.on(T::class.java, priority, action)
}

inline fun <reified T : Event> T.once(
    priority: ListenerPriority = ListenerPriority.NORMAL, noinline action: EventHandlerScope<T>.() -> Unit
): ListenerWrapper {
    return EventHandler.once(T::class.java, priority, action)
}

fun <T : Event> T.emit(event: T) {
    EventHandler.emit(event)
}

fun <T : Event> T.emitAndCheckCancel(event: T): Boolean {
    return EventHandler.emitAndCheckCancel(event)
}