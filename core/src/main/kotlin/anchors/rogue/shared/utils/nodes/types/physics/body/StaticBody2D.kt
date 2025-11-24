package anchors.rogue.shared.utils.nodes.types.physics.body

import anchors.rogue.shared.utils.nodes.core.Behavior
import anchors.rogue.shared.utils.nodes.core.Node
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef

class StaticBody2D(
    // Base props
    name: String,
    script: (node: StaticBody2D) -> Behavior<StaticBody2D>? = { null },
    position: Vector2 = Vector2.Zero,
    scale: Vector2 = Vector2(1F, 1F),
    rotation: Float = 0F,
    block: Node<*>.() -> Unit = {},
) : PhysicsBody2D<StaticBody2D>(
        name,
        script,
        position,
        scale,
        rotation,
        bodyType = BodyDef.BodyType.StaticBody,
        block,
    ) {
    override fun nodePhysicsUpdate(delta: Float) {
        super.nodePhysicsUpdate(delta)
        body.setTransform(globalPosition.x, globalPosition.y, rotation)
    }
}
