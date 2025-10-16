package anchors.rogue.ecs.graphics.components

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World
import com.github.quillraven.fleks.World.Companion.inject

/**
 * SpriteComponent holds a Sprite for rendering an entity.
 * It contains a single property, image, which is of type Sprite.
 * This component is used for entities that need to be visually represented in the game world.
 */
data class SpriteComponent(
    val image: Sprite,
) : Component<SpriteComponent> {
    override fun type() = SpriteComponent

    companion object : ComponentType<SpriteComponent>()
}
