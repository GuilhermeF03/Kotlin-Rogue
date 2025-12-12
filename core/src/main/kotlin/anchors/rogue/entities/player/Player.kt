package anchors.rogue.entities.player

import anchors.rogue.shared.utils.data.assets.FileSource
import anchors.rogue.shared.utils.nodes.core.Behavior
import anchors.rogue.shared.utils.nodes.core.nodeRef
import anchors.rogue.shared.utils.nodes.core.unaryPlus
import anchors.rogue.shared.utils.nodes.types.camera.Camera2D
import anchors.rogue.shared.utils.nodes.types.physics.body.DynamicBody2D
import anchors.rogue.shared.utils.nodes.types.physics.fixture.Collider2D
import anchors.rogue.shared.utils.nodes.types.physics.shape.CircleShape2D
import anchors.rogue.shared.utils.nodes.types.visual.Sprite2D
import anchors.rogue.shared.utils.nodes.types.visual.animation.AnimationPlayer
import com.badlogic.gdx.graphics.Texture.TextureFilter.Linear
import com.badlogic.gdx.math.Vector2

class Player(
    name: String = "player",
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

            Camera2D("camera", targetRef = nodeRef(this))
            Sprite2D("sprite", texture = texture)
            Collider2D("body", shape = CircleShape2D(200f))

            AnimationPlayer("animationPlayer")

            // Call for custom nodes ---
            block()
        },
    ) {
    val animationPlayer = nodeRef<AnimationPlayer>("animationPlayer")
}
