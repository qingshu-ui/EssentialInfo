package me.qingshu.essentialinfo.events

class ListenerWrapper(
    private val priority: ListenerPriority = ListenerPriority.NORMAL,
    val action: (Event) -> Unit
) : Comparable<ListenerWrapper> {
    var unregister: (() -> Boolean)? = null
    override fun compareTo(other: ListenerWrapper): Int {
        return other.priority.ordinal - this.priority.ordinal
    }

    fun off(): Boolean {
        return unregister?.invoke() ?: false
    }
}

enum class ListenerPriority {
    HIGHEST,
    HIGH,
    NORMAL,
    LOW,
    LOWEST
}