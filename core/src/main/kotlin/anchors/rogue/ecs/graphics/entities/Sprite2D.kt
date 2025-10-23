package anchors.rogue.ecs.graphics.entities

import anchors.rogue.ecs.graphics.components.SpriteComponent
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Vector2
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.EntityUpdateContext
import com.github.quillraven.fleks.World

/**
 * Creates a renderable entity with a SpriteComponent and PositionComponent.
 *
 * @param image The texture to be used for the sprite.
 * @param position The initial position of the entity in 2D space. Defaults to (0,0).
 * @param block An optional configuration block that allows further customization of the entity.
 * @return The created entity with the specified components and configurations.
 */
fun World.sprite2D(
    image: Texture,
    position: Vector2 = Vector2(0f, 0f),
    block: EntityUpdateContext.(Entity) -> Unit = {}, // For further configuration
) = entity {
    it += SpriteComponent(Sprite(image))
    it[SpriteComponent].image.translate(position.x, position.y)
}.configure(block)
