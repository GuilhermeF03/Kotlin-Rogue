package anchors.rogue.shared.utils.nodes.systems.physics

import anchors.rogue.shared.utils.nodes.core.GlobalNodeSystem
import anchors.rogue.shared.utils.nodes.core.UpdatePhase
import anchors.rogue.shared.utils.nodes.types.physics.body.PhysicsBody2D
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.badlogic.gdx.physics.box2d.World
import ktx.box2d.createWorld
import ktx.log.logger

class PhysicsSystem :
GlobalNodeSystem(
    phase = UpdatePhase.PhysicsBeforeScene,
    PhysicsBody2D::class,
) {
    private val logger = logger<PhysicsSystem>()
    val debugRenderer = Box2DDebugRenderer()


    // Physics world
    private var world: World? = null
    private val contactListener = PhysicsContactListener()

    override fun onSystemInit() {
        sceneManager.registerInjectable(World::class) { world }
    }

    override fun afterProcess(delta: Float) {
        val worldSnapshot = world ?: return
        debugRenderer.render(world, sceneManager.activeCamera?.camera?.combined)
        worldSnapshot.step(delta, 6, 2)
    }

    fun replaceWorld(gravity: Vector2 = Vector2.Zero) {
        world?.dispose()
        world = createWorld(gravity).apply {
            // Contact callbacks
            setContactListener(contactListener)
        }
    }
}
