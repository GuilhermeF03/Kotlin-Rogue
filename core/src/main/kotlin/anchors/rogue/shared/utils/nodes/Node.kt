package anchors.rogue.shared.utils.nodes

import com.badlogic.gdx.math.Vector2
import ktx.math.plus

@DslMarker
annotation class SceneDSL

@SceneDSL
open class Node(
    val name: String,
    script: Behavior.Factory<Node, Behavior<Node>>? = null,
    var position: Vector2 = Vector2.Zero.cpy(),
    var scale: Vector2 = Vector2(1f, 1f),
    var rotation: Float = 0f,
    block: Node.() -> Unit = {},
) {
    private val script = script?.create(this)

    var parent: Node? = null
        private set
    val children: MutableMap<String, Node> = mutableMapOf()

    val globalPosition: Vector2
        get() = position + (parent?.globalPosition ?: Vector2.Zero)
    val globalRotation: Float
        get() = rotation + (parent?.globalRotation ?: 0f)

    companion object {
        private val currentParent = ThreadLocal.withInitial<Node?> { null }
    }

    init {
        val parent = currentParent.get()
        parent?.addChildInternal(this)

        val oldParent = currentParent.get()
        currentParent.set(this)

        block()

        currentParent.set(oldParent)
    }

    private fun addChildInternal(child: Node) {
        check(child.name !in children) { "Child with name '${child.name}' already exists" }
        children[child.name] = child
        child.parent = this
    }

    fun buildTree() {
        enterTree() // top→down attach event
        ready() // bottom→up initialization
    }

    fun getNode(path: String): Node? {
        val parts = path.split("/")
        var current: Node = this
        for (part in parts) {
            current = current.children[part] ?: return null
        }
        return current
    }

    // Lifecycle methods
    internal fun ready() {
        children.values.forEach { it.ready() }
        script?.onReady()
    }

    internal fun enterTree() {
        script?.onEnterTree()
        children.values.forEach { it.enterTree() }
    }

    internal fun exitTree() {
        children.values.forEach { it.exitTree() }
        script?.onExitTree()
    }

    internal fun update(delta: Float) {
        children.values.forEach { it.update(delta) }
        script?.onUpdate(delta)
    }

    internal fun physicsUpdate(delta: Float) {
        children.values.forEach { it.physicsUpdate(delta) }
        script?.onPhysicsUpdate(delta)
    }
}
