package com.example.dialogstack

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.PriorityQueue

class DialogStack {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default.limitedParallelism(1))

    private val queue = PriorityQueue<DialogWrapper> { o1, o2 ->
        val priorityCompare = o2.priority.compareTo(o1.priority)
        if (priorityCompare != 0) priorityCompare else o2.timestamp.compareTo(o1.timestamp)
    }

    private val _dialogState = MutableStateFlow<DialogItem?>(null)
    val dialogState: StateFlow<DialogItem?> = _dialogState.asStateFlow()

    fun push(dialog: DialogItem) {
        scope.launch {
            queue.add(DialogWrapper(dialog))
            _dialogState.value = queue.peek()?.item
        }
    }

    fun pop() {
        scope.launch {
            queue.poll()
            _dialogState.value = queue.peek()?.item
        }
    }

    fun clear() {
        scope.launch {
            queue.clear()
            _dialogState.value = null
        }
    }

    fun isEmpty(): Boolean {
        return queue.isEmpty()
    }

    fun size(): Int {
        return queue.size
    }

    companion object {
        const val HIGH_PRIORITY = 100
        const val DEFAULT_PRIORITY = 50
        const val LOW_PRIORITY = 10
    }

    private data class DialogWrapper(
        val item: DialogItem,
        val timestamp: Long = System.currentTimeMillis()
    ) {
        val priority: Int get() = item.priority
    }
}