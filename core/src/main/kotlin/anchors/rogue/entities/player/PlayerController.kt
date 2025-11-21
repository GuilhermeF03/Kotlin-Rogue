package anchors.rogue.entities.player

import anchors.rogue.shared.utils.misc.toPixels
import anchors.rogue.shared.utils.nodes.core.Behavior
import anchors.rogue.shared.utils.nodes.types.physics.body.DynamicBody2D
import anchors.rogue.shared.utils.nodes.types.physics.fixture.Area2D
import anchors.rogue.shared.utils.nodes.types.physics.fixture.Collider2D
import ktx.log.logger
import ktx.math.plusAssign
import ktx.math.times

class PlayerController(node : DynamicBody2D) : Behavior<DynamicBody2D>(node) {

    private val logger = logger<PlayerController>()

    override fun onReady() {
        node?.apply {
            val area2D = getNode<Area2D>("body")

            area2D.areaEntered.connect { body -> logger.info { "Area entered: $body" } }
            area2D.areaExited.connect { body -> logger.info { "Area exited: $body" } }
            area2D.bodyEntered.connect { body -> logger.info { "Body entered: $body" } }
            area2D.bodyExited.connect { body -> logger.info { "Body exited: $body" } }

        }
    }

    override fun onPhysicsUpdate(delta: Float) {
        node?.apply {
            val inputVector = inputManager.getInputVector(
                "move-left",
                "move-right",
                "move-down",
                "move-up",
            ).nor() * delta * 200

            moveInstant(inputVector.toPixels())
        }
    }

}
