package anchors.rogue.shared.utils.nodes.types.visual.animation

import anchors.rogue.shared.utils.nodes.core.Behavior
import anchors.rogue.shared.utils.nodes.core.Node
import com.badlogic.gdx.math.Vector2

class AnimationPlayer(
    name: String,
    script: (node: AnimationPlayer) -> Behavior<AnimationPlayer>? = { null },
    position: Vector2 = Vector2.Zero,
    scale: Vector2 = Vector2(1f, 1f),
    rotation: Float = 0f,
    groups: MutableList<String> = mutableListOf(),
    block: AnimationPlayer.() -> Unit = {},
) : Node<AnimationPlayer>(
        name,
        script,
        position,
        scale,
        rotation,
        groups,
        block,
    ) {
    private val animations: MutableMap<String, Animation> = mutableMapOf()

    private var _time: Float = 0f // real accumulated time

    var time: Float
        get() {
            val anim = currentAnimation ?: return 0f
            return if (anim.loopable) _time % anim.length else _time.coerceAtMost(anim.length)
        }
        set(value) {
            val anim = currentAnimation ?: return

            if (!anim.loopable) {
                _time = value.coerceAtMost(anim.length)
                return
            }

            // For loopable animations
            val previousTime = _time
            val totalTime = previousTime + (value - previousTime) // delta increment

            val loops = (totalTime / anim.length).toInt()
            if (loops > 0) {
                repeat(loops) { anim.loopback() } // call loopback() for each full loop
            }

            // Keep _time within 0..length
            _time = totalTime % anim.length
        }

    var currentAnimation: Animation? = null
        private set

    fun addAnimation(animation: Animation) {
        animations[animation.name] = animation
    }

    fun removeAnimation(name: String) {
        animations.remove(name)
    }

    fun play(name: String) {
        val animation = animations[name]
        checkNotNull(animation)
        currentAnimation = animation
    }
}
