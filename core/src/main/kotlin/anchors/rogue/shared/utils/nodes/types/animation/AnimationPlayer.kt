package anchors.rogue.shared.utils.nodes.types.animation

import anchors.rogue.shared.utils.nodes.core.Behavior
import anchors.rogue.shared.utils.nodes.core.Node
import com.badlogic.gdx.math.Vector2

class AnimationPlayer(
    name: String,
    script: (node: AnimationPlayer) -> Behavior<AnimationPlayer>?,
    position: Vector2 = Vector2.Zero,
    scale: Vector2 = Vector2(1f, 1f),
    rotation: Float = 0f,
    groups: MutableList<String> = mutableListOf(),
    block: AnimationPlayer.() -> Unit,
) : Node<AnimationPlayer>(
    name,
    script,
    position,
    scale,
    rotation,
    groups,
    block
) {
    private val animations : MutableMap<String, Animation> = mutableMapOf()

    fun addAnimation(animation : Animation) {
        animations[animation.name] = animation
    }

    fun removeAnimation(name : String) {
        animations.remove(name)
    }

    fun play(name : String) {
        val animation = animations[name]
        checkNotNull(animation)

        animation.play()
    }
}
