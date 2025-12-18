package anchors.framework.nodes.types.visual.animation.tracks

import anchors.framework.nodes.types.visual.animation.Animation
import anchors.framework.nodes.types.visual.animation.Key

class ActionTrack(
    block: ActionTrack.() -> Unit = {},
) : Track<() -> Unit>() {
    override val maxLength: Float
        get() = Animation.currentParent.get()?.length ?: error("Track must be inside Animation block")

    companion object {
        private val currentParent = ThreadLocal.withInitial<ActionTrack?> { null }
    }

    init {
        val parentAnim = Animation.currentParent.get() ?: error("Track must be inside Animation block")
        parentAnim.addTrack(this)

        val old = currentParent.get()
        currentParent.set(this)

        block()

        currentParent.set(old)
    }

    override fun key(
        time: Float,
        value: () -> Unit,
    ) {
        check(keys.none { it.time == time }) { "Key $time already exists" }
        require(time <= maxLength) { "Key time outside animation length" }
        keys += Key(time, value)
    }

    override fun collectUpdates(
        prevTime: Float,
        time: Float,
    ): List<() -> Unit> =
        keys
            .filter { it.time > prevTime && it.time <= time }
            .map { it.value }
}
