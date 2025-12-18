package anchors.rogue.entities.player

import anchors.framework.data.assets.FileSource
import anchors.framework.nodes.core.Behavior
import anchors.framework.nodes.core.nodeRef
import anchors.framework.nodes.types.camera.Camera2D
import anchors.framework.nodes.types.physics.body.DynamicBody2D
import anchors.framework.nodes.types.physics.fixture.Collider2D
import anchors.framework.nodes.types.physics.shape.CircleShape2D
import anchors.framework.nodes.types.visual.Sprite2D
import anchors.framework.nodes.types.visual.animation.AnimationPlayer
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
            2000f,
        )
    },
    position: Vector2 = Vector2.Zero,
    scale: Vector2 = Vector2(1f, 1f),
    rotation: Float = 0f,
    groups: MutableList<String> = mutableListOf(),
    // DSL
    block: Player.() -> Unit = {},
) : DynamicBody2D<Player>(
        name,
        script,
        position,
        scale,
        rotation,
        groups,
        block = {
            // Root structure logic ---
            val texture =
                assetsManager.loadTexture("logo.png", source = FileSource.Internal) {
                    setFilter(Linear, Linear)
                }

            Camera2D("camera", nodeRef(this))
            Sprite2D("sprite", texture) // TODO: REPLACE WITH A *ANIMATED_SPRITE_2D* FOR ANIMATION
            Collider2D("body", CircleShape2D(200f))
            AnimationPlayer("animationPlayer")

            // Call for custom nodes ---
            block()
        },
    ) {
    val animationPlayer: AnimationPlayer by lazy { getNode("animationPlayer") }
}
