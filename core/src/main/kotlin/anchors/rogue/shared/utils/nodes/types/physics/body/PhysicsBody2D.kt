package anchors.rogue.shared.utils.nodes.types.physics.body

import anchors.rogue.shared.utils.misc.toPixels
import anchors.rogue.shared.utils.nodes.core.Behavior
import anchors.rogue.shared.utils.nodes.core.Node
import anchors.rogue.shared.utils.nodes.core.NodeDSL
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
    private val world = sceneManager.inject(World::class)
    val body = world.body(bodyType) {
        this.position.set(globalPosition) // This is fine inside the builder
        angle = rotation
    }

    // Only top-level physics bodies sync to nodes
    override fun physicsUpdate(delta: Float) {
        super.physicsUpdate(delta)
        val parentGlobal = parent?.globalPosition ?: Vector2.Zero
        //position = body.position.toPixels() - parentGlobal

        this.rotation = body.angle
    }

}
