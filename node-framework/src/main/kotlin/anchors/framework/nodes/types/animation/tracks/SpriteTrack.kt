package anchors.framework.nodes.types.animation.tracks

import anchors.framework.nodes.types.animation.SpriteAnimation
import anchors.framework.nodes.types.visual.AnimatedSprite2D
import com.badlogic.gdx.graphics.g2d.TextureRegion

class SpriteTrack<T : TextureRegion>(
    val sprite: AnimatedSprite2D<T>,
    vararg framesMappings : Pair<Float, Int>,
    block : SpriteTrack<T>.() -> Unit = {}
) : Track<T>() {

    private val mappings = framesMappings.toList()

    companion object {
        private val currentParent = ThreadLocal.withInitial<SpriteTrack<*>?> { null }
    }

    init {
        animation.addTrack(this)

        val old = currentParent.get()
        currentParent.set(this)
        block()
        currentParent.set(old)
    }

    override fun onAddTrack() {
        val parent = animation
        check(parent is SpriteAnimation<*>) {
            "SpriteTrack can only be used inside SpriteAnimation"
        }

        val frames = parent.frames

        mappings.forEach { (time, frame) ->
            key(time, frames[frame] as T)
        }
    }

    override fun collectUpdates(
        prevTime: Float,
        time: Float,
    ): List<() -> Unit> {
        val interpolatedValue = interpolateKeys(prevTime, time)
        return listOf { sprite.currFrame = interpolatedValue ?: sprite.currFrame }
    }
}
