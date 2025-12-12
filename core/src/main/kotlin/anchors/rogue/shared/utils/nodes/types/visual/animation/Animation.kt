package anchors.rogue.shared.utils.nodes.types.visual.animation

import anchors.rogue.shared.utils.nodes.types.visual.animation.tracks.Track

class Animation(
    val name: String,
    val length: Float,
    val loopable: Boolean = true,
    block: Animation.() -> Unit = {},
) {
    val tracks = mutableListOf<Track<*>>()

    companion object {
        val currentParent: ThreadLocal<Animation?> = ThreadLocal.withInitial { null }
    }

    init {
        val old = currentParent.get()
        currentParent.set(this)

        block()

        currentParent.set(old)
    }

    fun addTrack(track: Track<*>) {
        tracks.add(track)
    }

    fun loopback() {
        tracks.forEach { it.keys.forEach { key -> key.executed = false } }
    }
}
