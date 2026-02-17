package anchors.rogue.entities.player

import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import canopy.core.nodes.core.Behavior
import canopy.graphics.nodes.animation.Animation
import canopy.graphics.nodes.animation.tracks.ActionTrack
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode
import ktx.log.logger
import ktx.math.times

// CONSTANTS - Can  be tinkered in nodes as needed

const val PLAYER_SPEED = 1000f

class PlayerController(
    node: Player,
    // Custom parameters
    private val playerSpeed: Float = PLAYER_SPEED,
) : Behavior<Player>(node) {
    private val logger = logger<PlayerController>()

    @OptIn(ExperimentalTime::class)
    override fun onReady() {
        var timeRef = Clock.System.now()

        val testAnimation =
            Animation("test", 2.0f, PlayMode.LOOP) {
                ActionTrack {
                    key(0.5f) {
                        val ref = Clock.System.now()
                        logger.info { "Log: Animation time: 0.5, real time : ${ref.minus(timeRef)}" }
                    }
                    key(1f) {
                        val ref = Clock.System.now()
                        logger.info { "Log: Animation time: 1.0, real time : ${ref.minus(timeRef)}" }
                    }
                    key(1.5f) {
                        val ref = Clock.System.now()
                        logger.info { "Log: Animation time: 1.5, real time : ${ref.minus(timeRef)}" }
                        timeRef = ref
                    }
                }
            }

//        node?.apply {
//            animationPlayer.addAnimation(testAnimation)
//            animationPlayer.play("test")
//        }
    }

    override fun onPhysicsUpdate(delta: Float) {
//        node?.apply {
//            val inputVector =
//                inputManager
//                    .getInputVector(
//                        "move-left",
//                        "move-right",
//                        "move-down",
//                        "move-up",
//                    ) * delta * playerSpeed
//
//            moveInstant(inputVector.toPixels())
//        }
    }
}
