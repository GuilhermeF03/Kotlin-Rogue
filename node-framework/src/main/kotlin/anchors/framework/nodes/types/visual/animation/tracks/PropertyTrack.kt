package anchors.framework.nodes.types.visual.animation.tracks

import anchors.framework.misc.interpolate
import anchors.framework.nodes.types.visual.animation.Animation
import anchors.framework.nodes.types.visual.animation.Key
import kotlin.reflect.KMutableProperty0

class PropertyTrack<P>(
    val property: KMutableProperty0<P>,
    block: PropertyTrack<P>.() -> Unit = {},
) : Track<P>() {
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

    override fun collectUpdates(
        prevTime: Float,
        time: Float,
    ): List<() -> Unit> {
        if (keys.isEmpty()) return emptyList()

        val prevKey = keys.lastOrNull { it.time <= time } ?: return emptyList()
        val nextKey = keys.firstOrNull { it.time > time } ?: prevKey

        val factor =
            if (prevKey === nextKey) {
                0f
            } else {
                (time - prevKey.time) / (nextKey.time - prevKey.time)
            }

        val prevValue = prevKey.value
        val nextValue = nextKey.value

        val value = prevValue.interpolate(nextValue, factor)

        return listOf { property.set(value) }
    }
}
