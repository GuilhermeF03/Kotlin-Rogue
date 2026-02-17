package anchors.rogue.entities.player

import canopy.core.managers.ManagersRegistry
import canopy.core.nodes.core.Behavior
import canopy.core.nodes.core.nodeRef
import canopy.data.assets.AssetsManager
import canopy.data.assets.FileSource
import canopy.graphics.nodes.camera.Camera2D
import canopy.graphics.nodes.visual.Sprite2D
import canopy.physics.nodes.body.DynamicBody2D
import canopy.physics.nodes.fixture.Collider2D
import canopy.physics.nodes.shape.CircleShape2D
import com.badlogic.gdx.graphics.Texture.TextureFilter.Linear
import com.badlogic.gdx.math.Vector2

/**
 * Player scene - represents the player
 */
class Player(
    name: String = "player",
    // Base node props
    script: (node: Player) -> Behavior<Player>? = {
        PlayerController(
            it,
            2000f
        )
    },
    position: Vector2 = Vector2.Zero,
    scale: Vector2 = Vector2(1f, 1f),
    rotation: Float = 0f,
    groups: MutableList<String> = mutableListOf(),
    // DSL
    block: Player.() -> Unit = {},
) : DynamicBody2D<Player>(
    name = name,
    script = script,
    position = position,
    scale = scale,
    rotation = rotation,
    groups = groups,
    block = {
        val assetsManager = ManagersRegistry.get(AssetsManager::class)
        // Root structure logic ---
        val texture =
            assetsManager.loadTexture("logo.png", source = FileSource.Internal) {
                setFilter(Linear, Linear)
            }

        Camera2D("camera", targetRef = nodeRef(this))
        Sprite2D("sprite", texture) // TODO: REPLACE WITH A *ANIMATED_SPRITE_2D* FOR ANIMATION
        Collider2D("body", CircleShape2D(200f))
        // AnimationPlayer("animationPlayer")

        // Call for custom nodes ---
        block()
    }
) {
    // val animationPlayer: AnimationPlayer by lazy { getNode("animationPlayer") }
}
