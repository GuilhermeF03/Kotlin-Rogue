package anchors.rogue.shared.utils.nodes.types.visual

import anchors.rogue.shared.utils.nodes.core.Behavior
import anchors.rogue.shared.utils.nodes.core.Node
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Vector2

class Sprite2D(
    // Node base props
    name: String,
    script: (node: Sprite2D) -> Behavior<Sprite2D>? = { null },
    position: Vector2 = Vector2.Zero,
    scale: Vector2 = Vector2(1f, 1f),
    rotation: Float = 0f,
    // Node specific props
    val texture: Texture,
    // DSL
    block: Node<*>.() -> Unit = {},
) : Node<Sprite2D>(
        name,
        script,
        position,
        scale,
        rotation,
        block = block,
    )
