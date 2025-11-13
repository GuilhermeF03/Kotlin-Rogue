package anchors.rogue.shared.utils.nodes.types.camera

import anchors.rogue.shared.utils.nodes.core.Node
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3

class Camera2D(
    name: String = "Camera2D",
    var zoom: Float = 1f,
    var enableSmoothing: Boolean = true,
    var smoothingSpeed: Float = 8f,
    block: Node<*>.() -> Unit = {},
) : Node<Camera2D>(name, block = block) {
    val camera = OrthographicCamera()
    private var target: Node<*>? = null
    private var isResizing = false

    var limitLeft: Float? = null
    var limitRight: Float? = null
    var limitTop: Float? = null
    var limitBottom: Float? = null

    override fun enterTree() {
        super.enterTree()
        manager.registerCamera(this)
    }

    override fun exitTree() {
        super.exitTree()
        manager.unregisterCamera(this)
    }

    override fun update(delta: Float) {
        updateCamera(delta)
    }

    private fun updateCamera(delta: Float) {
        if (isResizing) return // skip smoothing during resize

        camera.zoom = zoom
        val targetPos = target?.globalPosition ?: globalPosition
        val pos = camera.position

        if (enableSmoothing) {
            pos.x += (targetPos.x - pos.x) * smoothingSpeed * delta
            pos.y += (targetPos.y - pos.y) * smoothingSpeed * delta
        } else {
            pos.x = targetPos.x
            pos.y = targetPos.y
        }

        applyLimits()
        camera.update()
    }

    private fun applyLimits() {
        limitLeft?.let { camera.position.x = maxOf(camera.position.x, it) }
        limitRight?.let { camera.position.x = minOf(camera.position.x, it) }
        limitBottom?.let { camera.position.y = maxOf(camera.position.y, it) }
        limitTop?.let { camera.position.y = minOf(camera.position.y, it) }
    }

    fun setTarget(node: Node<*>) {
        target = node
    }

    fun clearTarget() {
        target = null
    }

    fun screenToWorld(screen: Vector2): Vector2 {
        camera.unproject(Vector3(screen.x, screen.y, 0f))
        return screen
    }

    fun worldToScreen(world: Vector2): Vector2 {
        camera.project(Vector3(world.x, world.y, 0f))
        return world
    }

    /** Snap camera to target immediately during resize */
    fun resize(
        worldWidth: Float,
        worldHeight: Float,
    ) {
        isResizing = true
        camera.setToOrtho(false, worldWidth, worldHeight)
        val targetPos = target?.globalPosition ?: globalPosition
        camera.position.set(targetPos.x, targetPos.y, 0f)
        camera.update()
        isResizing = false
    }
}
