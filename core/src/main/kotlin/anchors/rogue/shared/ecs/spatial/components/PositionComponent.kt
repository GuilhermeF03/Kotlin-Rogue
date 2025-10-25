package anchors.rogue.shared.ecs.spatial.components

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

/**
 * PositionComponent represents the position of an entity in 2D space.
 * It contains a Vector2 to store the x and y coordinates.
 * This component is used for entities that need to have a position in the game world.
 */
class PositionComponent(
    var x: Float,
    var y: Float,
) : Component<PositionComponent>,
    Comparable<PositionComponent> {
    override fun type() = PositionComponent

    override fun compareTo(other: PositionComponent) = y compareTo other.y

    companion object : ComponentType<PositionComponent>()
}
