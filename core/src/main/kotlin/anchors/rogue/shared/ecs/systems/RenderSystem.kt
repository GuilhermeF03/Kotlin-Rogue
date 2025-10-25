package anchors.rogue.shared.ecs.systems

import anchors.rogue.shared.ecs.graphics.components.SpriteComponent
import anchors.rogue.shared.ecs.spatial.components.PositionComponent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.github.quillraven.fleks.World.Companion.inject
import com.github.quillraven.fleks.collection.compareEntity

/**
 * RenderingSystem is responsible for rendering entities with SpriteComponent.
 * It processes each entity in the family and handles the rendering logic.
 */
class RenderSystem(
    private val stage: Stage = inject(),
) : IteratingSystem(
        family { all(PositionComponent, SpriteComponent) },
        compareEntity { e1, e2 -> e1[PositionComponent].y compareTo e2[PositionComponent].y }, // depth sorting by Y
    ) {
    override fun onUpdate() {
        with(stage) {
            viewport.apply()
            act(deltaTime)
            draw()
        }
    }

    override fun onTickEntity(entity: Entity) {
        val p = entity[PositionComponent]
        val actor = entity[SpriteComponent].image
        actor.setPosition(p.x, p.y)
    }
}
