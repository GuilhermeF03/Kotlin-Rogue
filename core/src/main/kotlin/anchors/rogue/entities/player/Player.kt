package anchors.rogue.entities.player

import com.badlogic.gdx.graphics.Texture.TextureFilter.Linear
import io.canopy.engine.core.managers.manager
import io.canopy.engine.core.nodes.core.attachBehavior
import io.canopy.engine.core.nodes.core.nodeRef
import io.canopy.engine.data.core.assets.AssetsManager
import io.canopy.engine.graphics.nodes.camera.Camera2D
import io.canopy.engine.graphics.nodes.visual.Sprite2D
import io.canopy.engine.physics.nodes.body.DynamicBody2D
import io.canopy.engine.physics.nodes.fixture.Collider2D
import io.canopy.engine.physics.nodes.shape.CircleShape2D

/**
 * Player scene - represents the player
 */
class Player(
    name: String = "player",
    private val speed: Float = 2000f,
    // DSL
    block: Player.() -> Unit = {},
) : DynamicBody2D<Player>(name, block) {
    override fun create() {
        super.create()

        attachBehavior { PlayerController(it, playerSpeed = speed) }

        val assetsManager = manager<AssetsManager>()
        // Root structure logic ---
        val texture =
            assetsManager.loadTexture("logo.png", source = AssetsManager.FileSource.Internal) {
                setFilter(Linear, Linear)
            }

        Camera2D("camera", targetRef = nodeRef(this))
        Sprite2D("sprite", texture) // TODO: REPLACE WITH A *ANIMATED_SPRITE_2D* FOR ANIMATION
        Collider2D("body", CircleShape2D(200f))
    }
}
