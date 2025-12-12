package anchors.rogue.shared.utils.nodes.types.visual.animation.tracks

class Key<T> internal constructor(
    val time: Float,
    val value: T,
) {
    var executed: Boolean = false
}
