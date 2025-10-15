package anchors.rogue.ecs.systems

import anchors.rogue.ecs.components.PositionComponent
import anchors.rogue.ecs.components.SpriteComponent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.github.quillraven.fleks.World.Companion.inject
import com.github.quillraven.fleks.collection.compareEntityBy
import ktx.graphics.use
import ktx.log.logger


/**
 * RenderingSystem is responsible for rendering entities with SpriteComponent.
 * It processes each entity in the family and handles the rendering logic.
 */
class RenderSystem(
    private val stage: Stage = inject(),
) : IteratingSystem(
    family { all(PositionComponent, SpriteComponent) },
    compareEntityBy(PositionComponent)
) {
    override fun onInit() {
        super.onInit()
        logger.info { "Render system bootstrapped!" }
    }

    override fun onTick() {
        with(stage){
            viewport.apply()
            batch.use {
                super.onTick()
            }
            act()
            draw()
        }
    }
    override fun onTickEntity(entity: Entity) {
        val pos = entity[PositionComponent].coords
        val sprite = entity[SpriteComponent].image

        val imageActor = Image(sprite.texture)
        imageActor.setPosition(pos.x, pos.y)
        stage.addActor(imageActor)
    }

    companion object{
        val logger = logger<RenderSystem>()
    }
}
