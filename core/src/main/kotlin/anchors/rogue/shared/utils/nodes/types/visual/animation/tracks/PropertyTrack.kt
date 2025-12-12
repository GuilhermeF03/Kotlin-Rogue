package anchors.rogue.shared.utils.nodes.types.visual.animation.tracks

import anchors.rogue.shared.utils.nodes.types.visual.animation.Animation
import kotlin.reflect.KMutableProperty0

class PropertyTrack<P>(
    val property: KMutableProperty0<P>,
    block: PropertyTrack<P>.() -> Unit = {},
) : Track<P> {
    override val keys = mutableListOf<Key<P>>()

    override val maxLength: Float
        get() = Animation.currentParent.get()?.length ?: error("Track must be inside Animation block")

    init {
        val parentAnim = Animation.currentParent.get() ?: error("Track must be inside Animation block")
        parentAnim.addTrack(this)
        block()
    }

    override fun key(
        time: Float,
        value: P,
    ) {
        check(keys.none { it.time == time }) { "Key $time already exists" }
        require(time <= maxLength) { "Key time outside animation length" }
        keys += Key(time, value)
    }
}
