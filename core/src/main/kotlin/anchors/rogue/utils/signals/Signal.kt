package anchors.rogue.utils.signals

import java.lang.ref.WeakReference
import java.util.concurrent.CopyOnWriteArrayList

/**
 * Generic weak-reference signal-slot implementation.
 * Supports 0..3 arguments.
 */
sealed interface Signal {

    fun clear()

    // Helper class for managing weak listeners
    private class WeakListeners<T : Any> {
        val listeners = CopyOnWriteArrayList<WeakReference<T>>()

        fun add(listener: T) {
            listeners += WeakReference(listener)
        }

        fun remove(listener: T) {
            listeners.removeIf { it.get() == listener || it.get() == null }
        }

        fun forEach(action: (T) -> Unit) {
            val iterator = listeners.iterator()
            while (iterator.hasNext()) {
                val listener = iterator.next().get()
                if (listener != null) {
                    action(listener)
                } else {
                    iterator.remove()
                }
            }
        }

        fun clear() = listeners.clear()
    }

    /** 0-argument signal */
    class NoArgSignal : Signal {
        private val callbacks = WeakListeners<() -> Unit>()

        infix fun connect(listener: () -> Unit) = callbacks.add(listener)
        infix fun disconnect(listener: () -> Unit) = callbacks.remove(listener)

        fun emit() = callbacks.forEach { it() }
        operator fun invoke() = emit()
        override fun clear() = callbacks.clear()
    }

    /** 1-argument signal */
    class OneArgSignal<A> : Signal {
        private val callbacks = WeakListeners<(A) -> Unit>()

        infix fun connect(listener: (A) -> Unit) = callbacks.add(listener)
        infix fun disconnect(listener: (A) -> Unit) = callbacks.remove(listener)

        fun emit(a: A) = callbacks.forEach { it(a) }
        operator fun invoke(a: A) = emit(a)
        override fun clear() = callbacks.clear()
    }

    /** 2-argument signal */
    class TwoArgsSignal<A, B> : Signal {
        private val callbacks = WeakListeners<(A, B) -> Unit>()

        infix fun connect(listener: (A, B) -> Unit) = callbacks.add(listener)
        infix fun disconnect(listener: (A, B) -> Unit) = callbacks.remove(listener)

        fun emit(a: A, b: B) = callbacks.forEach { it(a, b) }
        operator fun invoke(a: A, b: B) = emit(a, b)
        override fun clear() = callbacks.clear()
    }

    /** 3-argument signal */
    class ThreeArgsSignal<A, B, C> : Signal {
        private val callbacks = WeakListeners<(A, B, C) -> Unit>()

        infix fun connect(listener: (A, B, C) -> Unit) = callbacks.add(listener)
        infix fun disconnect(listener: (A, B, C) -> Unit) = callbacks.remove(listener)

        fun emit(a: A, b: B, c: C) = callbacks.forEach { it(a, b, c) }
        operator fun invoke(a: A, b: B, c: C) = emit(a, b, c)
        override fun clear() = callbacks.clear()
    }
}

// Factory functions
fun signal() = Signal.NoArgSignal()
inline fun <reified A> signal() = Signal.OneArgSignal<A>()
inline fun <reified A, reified B> signal() = Signal.TwoArgsSignal<A, B>()
inline fun <reified A, reified B, reified C> signal() = Signal.ThreeArgsSignal<A, B, C>()
