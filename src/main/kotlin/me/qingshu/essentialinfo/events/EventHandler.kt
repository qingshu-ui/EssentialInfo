package me.qingshu.essentialinfo.events

import me.qingshu.essentialinfo.Essentialinfo
import java.util.*

interface EventHandlerScope<T : Event> {
    var event: T
}

object EventHandler {
    private val listeners = mutableMapOf<Class<*>, PriorityQueue<ListenerWrapper>>()
    private val log = Essentialinfo.logger

    @JvmStatic
    @JvmOverloads
    fun <T : Event> on(
        event: Class<T>,
        priority: ListenerPriority = ListenerPriority.NORMAL,
        action: EventHandlerScope<T>.() -> Unit
    ): ListenerWrapper {
        val c = listeners.getOrPut(event) { PriorityQueue() }
        val wrapper = ListenerWrapper(priority) { e ->
            object : EventHandlerScope<T> {
                @Suppress("UNCHECKED_CAST")
                override var event: T = e as T
            }.run(action)
        }.apply {
            unregister = { c.remove(this) }
        }
        c.add(wrapper)
        return wrapper
    }

    @JvmStatic
    fun <T : Event> off(event: Class<T>) {
        listeners.remove(event)
    }

    @JvmStatic
    fun <T : Event> emit(event: T) {
        listeners[event.javaClass]?.toList()?.forEach { wrapper ->
            if (event is CancellableEvent) event.cancel = false
            kotlin.runCatching {
                wrapper.action(event)
                if (event is CancellableEvent && event.cancel) return@forEach
            }.onFailure {
                log.warn("Event $event execute failed, {}, {}", it.message, it)
            }
        }
    }

    @JvmStatic
    fun <T : Event> emitAndCheckCancel(event: T): Boolean {
        emit(event)
        return event is CancellableEvent && event.cancel
    }

    @JvmStatic
    @JvmOverloads
    fun <T : Event> once(
        event: Class<T>, priority: ListenerPriority = ListenerPriority.NORMAL, action: EventHandlerScope<T>.() -> Unit
    ): ListenerWrapper {
        lateinit var wrapper: ListenerWrapper
        wrapper = on(event, priority) {
            action()
            wrapper.off()
        }
        return wrapper
    }
}