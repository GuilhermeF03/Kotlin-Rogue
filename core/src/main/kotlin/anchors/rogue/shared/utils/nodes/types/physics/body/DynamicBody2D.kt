package anchors.rogue.shared.utils.nodes.types.physics.body

import anchors.rogue.shared.utils.nodes.core.Behavior
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef

open class DynamicBody2D<T : DynamicBody2D<T>>(
    name: String,
    script: (node: T) -> Behavior<T>? = { null },
    position: Vector2 = Vector2.Zero,
    scale: Vector2 = Vector2(1F, 1F),
    rotation: Float = 0F,
    groups: MutableList<String> = mutableListOf(),
    block: T.() -> Unit = {},
) : PhysicsBody2D<T>(
        name,
        script,
        position,
        scale,
        rotation,
        bodyType = BodyDef.BodyType.DynamicBody,
        groups,
        block,
    ) {
    /**
     * ⚡ Instant velocity movement (teleports velocity, not the body).
     * Good for direct controller movement.
     *
     * Example: character walking, flying, sliding.
     */
    fun moveInstant(velocity: Vector2) {
        body.linearVelocity = velocity
    }

    /**
     * 🌀 Smooth force-based movement.
     * Good for heavy bodies, vehicles, dragging effects.
     *
     * Add smoothing factor if needed.
     */
    fun moveSmooth(force: Vector2) {
        TODO("NEED TO FIX MOVEMENT")
        body.applyForceToCenter(force, true)
    }

    /**
     * 💥 Impulse burst.
     * Good for jumps, dashes, recoil, knockback.
     */
    fun moveImpulse(impulse: Vector2) {
        TODO("NEED TO FIX MOVEMENT")
        body.applyLinearImpulse(impulse, body.worldCenter, true)
    }

    /**
     * Teleports the body (allowed for dynamic bodies).
     * Use only for respawn or heavy repositioning.
     */
    fun teleport(position: Vector2) {
        body.setTransform(position, body.angle)
        body.isAwake = true
    }

    /**
     * Convenience: stop the body instantly.
     */
    fun stop() {
        body.linearVelocity.set(0f, 0f)
        body.angularVelocity = 0f
    }
}
