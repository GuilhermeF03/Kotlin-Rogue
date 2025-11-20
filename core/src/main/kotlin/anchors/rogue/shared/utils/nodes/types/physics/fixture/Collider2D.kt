package anchors.rogue.shared.utils.nodes.types.physics.fixture

import anchors.rogue.shared.utils.nodes.core.Behavior
import anchors.rogue.shared.utils.nodes.core.Node
import anchors.rogue.shared.utils.nodes.types.physics.body.PhysicsBody2D
import anchors.rogue.shared.utils.nodes.types.physics.shape.PhysicsShape2D
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.Filter
import com.badlogic.gdx.physics.box2d.Fixture
import com.badlogic.gdx.physics.box2d.Shape
import ktx.box2d.fixture

class Collider2D(
    // Base props
    name : String,
    script : (node : Collider2D) -> Behavior<Collider2D>? = { null },
    position : Vector2 = Vector2.Zero,
    scale : Vector2 = Vector2(1F, 1F),
    rotation : Float = 0F,

    // Specific props
    var shape : PhysicsShape2D,
    val angle : Float = 0F,
    var friction: Float = 0.2f,
    var restitution: Float = 0.0f,
    var density: Float = 0.0f,
    val filter: Filter = Filter(),

    // DSL
    block : Node<*>.() -> Unit = {}
) : Node<Collider2D>(
    name,
    script,
    position,
    scale,
    rotation,
    block
){
    private var fixture : Fixture? = null

    override fun enterTree() {
        val parentBody = (parent as? PhysicsBody2D)?.body ?: return
        fixture = shape.shapeFactory(
            this,
            parentBody,
            position,
            angle,
            friction,
            restitution,
            density,
            filter,
            false
        )
    }

    override fun exitTree() {
        val parentBody = (parent as? PhysicsBody2D)?.body ?: return
        val fixtureSnapshot = fixture ?: return
        parentBody.destroyFixture(fixtureSnapshot)
    }
}
