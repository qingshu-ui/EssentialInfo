package me.qingshu.essentialinfo.events

class ListenerWrapper(
    private val priority: ListenerPriority = ListenerPriority.NORMAL,
    val action: (Event) -> Unit,
) : Comparable<ListenerWrapper> {
    var unregister: (() -> Boolean)? = null

    override fun compareTo(other: ListenerWrapper): Int = this.priority.ordinal - other.priority.ordinal

    fun off(): Boolean = unregister?.invoke() ?: false
}

enum class ListenerPriority {
    HIGHEST,
    HIGH,
    NORMAL,
    LOW,
    LOWEST,
}
