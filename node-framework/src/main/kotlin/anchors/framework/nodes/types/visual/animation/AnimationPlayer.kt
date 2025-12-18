package anchors.framework.nodes.types.visual.animation

import anchors.framework.nodes.core.Behavior
import anchors.framework.nodes.core.Node
import anchors.framework.systems.AnimationSystem
import com.badlogic.gdx.math.Vector2
import ktx.log.logger
import kotlin.math.abs

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
    private val animations = mutableMapOf<String, Animation>()
    private val logger = logger<AnimationPlayer>()

    private var _time = 0f
    private var direction = 1 // 1 = forward, -1 = backward

    var currentAnimation: Animation? = null
        private set

    val time: Float
        get() = _time

    init {
        if (!sceneManager.hasSystem(AnimationSystem::class)) {
            logger.error {
                """
                [ANIMATION PLAYER]
                No AnimationSystem registered.
                Animations will not update!
                """.trimIndent()
            }
        }
    }

    fun addAnimation(animation: Animation) {
        animations[animation.name] = animation
    }

    fun removeAnimation(name: String) {
        animations.remove(name)
    }

    fun play(name: String) {
        val anim = animations[name] ?: error("Animation '$name' not found")

        currentAnimation = anim

        when (anim.playMode) {
            PlayMode.LOOP_REVERSED,
            PlayMode.REVERSED,
            -> {
                _time = anim.length
                direction = -1
            }

            else -> {
                _time = 0f
                direction = 1
            }
        }
    }

    /**
     * Update animation time.
     * Call this once per frame from AnimationSystem.
     */
    fun update(delta: Float) {
        val anim = currentAnimation ?: return
        if (anim.length <= 0f || delta == 0f) return

        var remainingDelta = abs(delta)

        while (remainingDelta > 0f) {
            val timeToEdge =
                if (direction > 0) {
                    anim.length - _time
                } else {
                    _time
                }

            if (remainingDelta < timeToEdge) {
                _time += remainingDelta * direction
                break
            }

            // Hit an edge
            _time += timeToEdge * direction
            remainingDelta -= timeToEdge

            when (anim.playMode) {
                PlayMode.NORMAL -> {
                    _time = anim.length
                    return
                }

                PlayMode.REVERSED -> {
                    _time = 0f
                    return
                }

                PlayMode.LOOP -> {
                    anim.loopback()
                    _time = 0f
                }

                PlayMode.LOOP_REVERSED -> {
                    anim.loopback()
                    _time = anim.length
                }

                PlayMode.LOOP_PINGPONG -> {
                    anim.loopback()
                    direction *= -1
                }
                else -> {}
            }
        }
    }
}
