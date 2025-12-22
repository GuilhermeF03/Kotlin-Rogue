package anchors.framework.nodes.types.animation

import anchors.framework.nodes.types.animation.tracks.Track

typealias PlayMode = com.badlogic.gdx.graphics.g2d.Animation.PlayMode

@Suppress("UNCHECKED_CAST")
open class Animation<T : Animation<T>>(
    val name: String,
    initialLength : Float? = null,
    val playMode: PlayMode = PlayMode.NORMAL,
    block: T.() -> Unit = {},
) {
    constructor(
        name: String,
        playMode: PlayMode = PlayMode.NORMAL,
        block: T.() -> Unit = {}
    ) : this(name, null, playMode, block)

    private var _length : Float? = initialLength

    open var length: Float
        get() = _length ?: tracks.maxOfOrNull { it.maxLength } ?: 0f
        set(value) {_length = value}

    val tracks = mutableListOf<Track<*>>()

    companion object {
        val currentParent: ThreadLocal<Animation<*>?> = ThreadLocal.withInitial { null }
    }

    init {
        val old = currentParent.get()
        currentParent.set(this)

        block(this as T)

        //currentParent.set(old)

        // 🔑 NOW the animation is fully constructed
        finalizeTracks()
    }

    private fun finalizeTracks() {
        tracks.forEach { it.onAddTrack() }
    }


    fun addTrack(track: Track<*>) {
        tracks += track
    }

    fun loopback() {
        tracks.forEach { it.loopback() }
    }
}
