package anchors.framework.nodes.types.visual.animation

class Key<T> internal constructor(
    val time: Float,
    val value: T,
) {
    var executed: Boolean = false
}
