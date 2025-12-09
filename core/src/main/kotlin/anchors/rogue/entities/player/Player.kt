package anchors.rogue.entities.player

import anchors.rogue.shared.utils.data.assets.FileSource
import anchors.rogue.shared.utils.nodes.core.Behavior
import anchors.rogue.shared.utils.nodes.core.Node
import anchors.rogue.shared.utils.nodes.core.nodeRef
import anchors.rogue.shared.utils.nodes.types.camera.Camera2D
import anchors.rogue.shared.utils.nodes.types.physics.body.DynamicBody2D
import anchors.rogue.shared.utils.nodes.types.physics.fixture.Collider2D
import anchors.rogue.shared.utils.nodes.types.physics.shape.CircleShape2D
import anchors.rogue.shared.utils.nodes.types.visual.Sprite2D
import com.badlogic.gdx.graphics.Texture.TextureFilter.Linear
import com.badlogic.gdx.math.Vector2

class Player internal constructor(
    name : String = "player",
    script : (node : Player) -> Behavior<Player>? = { null },
    position: Vector2 = Vector2.Zero,
    scale: Vector2 = Vector2(1f, 1f),
    rotation: Float = 0f,
    groups: MutableList<String> = mutableListOf(),
    block : Player.() -> Unit = {}
) : DynamicBody2D<Player>(
    name,
    script,
    position,
    scale,
    rotation,
    groups,
    block
)


@Suppress("ktlint:standard:function-naming")
fun Player(
    position: Vector2 = Vector2.Zero,
    scale: Vector2 = Vector2(1f, 1f),
    rotation: Float = 0f,
    groups: MutableList<String> = mutableListOf(),
    block: Player.() -> Unit = {},
) = Player(
    name = "player",
    script = { node -> PlayerController(node) },
    position,
    scale,
    rotation,
    groups,
) {
    val texture =
        assetsManager.loadTexture("logo.png", source = FileSource.Internal) {
            setFilter(Linear, Linear)
        }

    Camera2D("camera", targetRef = nodeRef(this))
    Sprite2D("sprite", texture = texture)
    Collider2D("body", shape = CircleShape2D(200f))

    block()
}
