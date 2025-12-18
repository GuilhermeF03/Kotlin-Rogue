package anchors.framework.nodes.types.visual.animation.tracks

import anchors.framework.nodes.types.visual.animation.Key

abstract class Track<T> {
    abstract val maxLength: Float
    protected val keys: MutableList<Key<T>> = mutableListOf()

    val keyCount: Int
        get() = keys.size

    /**
     * Add new key
     */
    abstract fun key(
        time: Float,
        value: T,
    )

    /**
     * Collects side effects that should happen between prevTime and time.
     * Returned lambdas will be executed later.
     */
    abstract fun collectUpdates(
        prevTime: Float,
        time: Float,
    ): List<() -> Unit>

    fun loopback() = keys.forEach { it.executed = false }
}
