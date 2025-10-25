package anchors.rogue.shared.ecs.graphics.entities

import anchors.rogue.shared.ecs.graphics.components.SpriteComponent
import anchors.rogue.shared.ecs.spatial.components.PositionComponent
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.EntityUpdateContext
import com.github.quillraven.fleks.World

/**
 * Creates a renderable entity with a SpriteComponent and PositionComponent.
 *
 * @param texture The texture to be used for the sprite.
 * @param position The initial position of the entity in 2D space. Defaults to (0,0).
 * @param block An optional configuration block that allows further customization of the entity.
 * @return The created entity with the specified components and configurations.
 */
fun World.sprite2D(
    texture: Texture,
    position: Vector2 = Vector2(0f, 0f),
    block: EntityUpdateContext.(Entity) -> Unit = {}, // For further configuration
) {
    val stage = inject<Stage>()

    val imageActor = Image(texture)
    imageActor.setPosition(position.x, position.y)
    stage.addActor(imageActor)

    return entity {
        it += PositionComponent(position.x, position.y)
        it += SpriteComponent(imageActor)
    }.configure(block)
}
