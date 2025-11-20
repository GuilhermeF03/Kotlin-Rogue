package anchors.rogue.shared.utils.nodes.types.physics.shape

import anchors.rogue.shared.utils.nodes.core.Node
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.Filter
import com.badlogic.gdx.physics.box2d.Fixture
import com.badlogic.gdx.physics.box2d.Shape

interface PhysicsShape2D {
    fun shapeFactory(
        fixtureNode : Node<*>,
        body: Body,
        position: Vector2,
        angle: Float,
        friction: Float,
        restitution: Float,
        density: Float,
        filter: Filter,
        isSensor: Boolean
    ) : Fixture
}
