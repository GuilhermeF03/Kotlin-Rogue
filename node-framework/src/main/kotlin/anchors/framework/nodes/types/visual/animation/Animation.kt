package anchors.framework.nodes.types.visual.animation

import anchors.framework.nodes.types.visual.animation.tracks.Track

typealias PlayMode = com.badlogic.gdx.graphics.g2d.Animation.PlayMode

class Animation(
    val name: String,
    val length: Float,
    val playMode: PlayMode = PlayMode.NORMAL,
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
        tracks.forEach { it.loopback() }
    }
}
