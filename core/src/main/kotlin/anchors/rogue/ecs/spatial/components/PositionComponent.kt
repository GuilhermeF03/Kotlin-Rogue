package anchors.rogue.ecs.spatial.components

import com.badlogic.gdx.math.Vector2
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

/**
 * PositionComponent represents the position of an entity in 2D space.
 * It contains a Vector2 to store the x and y coordinates.
 * This component is used for entities that need to have a position in the game world.
 */
class PositionComponent(
    var coords: Vector2 = Vector2(0f, 0f),
) : Component<PositionComponent>, Comparable<PositionComponent> {
    override fun type() = PositionComponent

    override fun compareTo(other: PositionComponent) = coords.y compareTo other.coords.y
    companion object : ComponentType<PositionComponent>()
}
