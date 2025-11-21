package anchors.rogue.shared.utils.nodes.types.physics.body

import anchors.rogue.shared.utils.misc.toPixels
import anchors.rogue.shared.utils.nodes.core.Behavior
import anchors.rogue.shared.utils.nodes.core.Node
import anchors.rogue.shared.utils.nodes.core.NodeDSL
import anchors.rogue.shared.utils.signals.createSignal
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.World
import ktx.box2d.body
import ktx.log.logger
import ktx.math.minus

abstract class PhysicsBody2D<T : PhysicsBody2D<T>>(
    // Base props
    name: String,
    script: (node: T) -> Behavior<T>? = { null },
    position: Vector2 = Vector2.Zero,
    scale: Vector2 = Vector2(1F, 1F),
    rotation: Float = 0F,
    // Specific props
    val bodyType: BodyDef.BodyType,
    block: Node<*>.() -> Unit = {},
) : Node<T>(
        name,
        script,
        position,
        scale,
        rotation,
        block,
    ) {

    private val logger = logger<PhysicsBody2D<T>>()

    // ==========================
    //          Physics
    // ==========================
    private val world = sceneManager.inject(World::class)
    val body = world.body(bodyType) {
        this.position.set(globalPosition)
        angle = rotation
    }
    // Override position and rotation to force body movement and rotate methods
    override var position
        get() = if(body == null) super.position else (body.position - (parent?.globalPosition ?: Vector2.Zero))
        set(_) = throw IllegalAccessException("Manual position override not allowed on physics nodes")

    override var rotation: Float
        get() = if(body == null) super.rotation else (body.angle - (parent?.globalRotation ?: 0f))
        set(_) = throw IllegalAccessException("Manual rotation override not allowed on physics nodes")
}
