package anchors.rogue.shared.utils.nodes.types.visual.animation.tracks

interface Track<T> {
    val maxLength: Float
    val keys: MutableList<Key<T>>

    fun key(
        time: Float,
        value: T,
    )
}
