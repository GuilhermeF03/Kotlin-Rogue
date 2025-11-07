package anchors.rogue.shared.utils.nodes.types.visual

import anchors.rogue.shared.ecs.managers.ManagersRegistry
import anchors.rogue.shared.utils.nodes.SceneManager
import anchors.rogue.shared.utils.nodes.core.Behavior
import anchors.rogue.shared.utils.nodes.core.Node
import anchors.rogue.shared.utils.nodes.types.empty.EmptyNode
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.ui.Image

class Sprite2D(
    // Node base props
    name : String,
    script: (node : Sprite2D) -> Behavior<Sprite2D>? = { null },
    position: Vector2 = Vector2.Zero,
    scale: Vector2 = Vector2(1f, 1f),
    rotation: Float = 0f,

    // Node specific props
    texture: Texture,

    // DSL
    block: Node<*>.() -> Unit = {},
): Node<Sprite2D>(
    name,
    script,
    position,
    scale,
    rotation,
    block = block
){
    var texture : Texture = texture
        set(value) = replaceImage(value)

    var image : Image
        private set
    init {
        image = Image(texture)
    }

    fun replaceImage(newTexture : Texture){
        image = Image(newTexture)
        image.setPosition(position.x, position.y)
    }



}
