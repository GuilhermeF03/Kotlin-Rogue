package anchors.rogue.shared.ecs.systems

import anchors.rogue.shared.ecs.graphics.components.SpriteComponent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.github.quillraven.fleks.World.Companion.inject
import com.github.quillraven.fleks.collection.compareEntity
import ktx.graphics.use
import ktx.log.logger

/**
 * RenderingSystem is responsible for rendering entities with SpriteComponent.
 * It processes each entity in the family and handles the rendering logic.
 */
class RenderSystem(
    private val stage: Stage = inject(),
) : IteratingSystem(
        family { any(SpriteComponent) },
        compareEntity { e1, e2 ->
            e1[SpriteComponent].image.y compareTo e2[SpriteComponent].image.y
        },
    ) {
    override fun onInit() {
        super.onInit()
        logger.info { "Render system bootstrapped!" }
    }

    override fun onUpdate() {
        with(stage) {
            viewport.apply()
            batch.use { super.onUpdate() }
            act(deltaTime)
            draw()
        }
    }

    override fun onTickEntity(entity: Entity) {
        val sprite = entity[SpriteComponent].image

        val imageActor = Image(sprite.texture)
        imageActor.setPosition(sprite.x, sprite.y)
        stage.addActor(imageActor)
    }

    companion object {
        val logger = logger<RenderSystem>()
    }
}
