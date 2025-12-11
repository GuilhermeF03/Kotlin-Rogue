package anchors.rogue.entities.player

import anchors.rogue.shared.utils.misc.toPixels
import anchors.rogue.shared.utils.nodes.core.Behavior
import ktx.log.logger
import ktx.math.times

const val PLAYER_SPEED = 1000f

class PlayerController(
    node: Player,
    // Custom parameters
    private val playerSpeed: Float = PLAYER_SPEED,
) : Behavior<Player>(node) {
    private val logger = logger<PlayerController>()

    override fun onPhysicsUpdate(delta: Float) {
        node?.apply {
            val inputVector =
                inputManager
                    .getInputVector(
                        "move-left",
                        "move-right",
                        "move-down",
                        "move-up",
                    ).nor() * delta * playerSpeed

            moveInstant(inputVector.toPixels())
        }
    }
}
