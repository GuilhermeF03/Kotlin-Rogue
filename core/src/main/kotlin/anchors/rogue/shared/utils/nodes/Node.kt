package anchors.rogue.shared.utils.nodes

import anchors.rogue.shared.ecs.managers.ManagersRegistry
import com.badlogic.gdx.math.Vector2
import ktx.math.plus

open class Node(
    val name: String,
    var position: Vector2 = Vector2.Zero.cpy(),
    var scale: Vector2 = Vector2(1f, 1f),
    var rotation: Float = 0f,
    factory: Behavior.Factory<Node, Behavior<Node>>? = null,
) {
    private val sceneTree = ManagersRegistry.get(SceneTreeManager::class)

    // External behaviors, like Godot scripts / Unity MonoBehaviours
    private val script = factory?.create(this)

    // Hierarchy
    var parent: Node? = null
        private set
    val children: MutableMap<String, Node> = mutableMapOf()

    // Global transform properties
    val globalPosition: Vector2
        get() = position + (parent?.globalPosition ?: Vector2.Zero)

    val globalRotation: Float
        get() = rotation + (parent?.globalRotation ?: 0f)

    // Child management
    fun addChild(child: Node) {
        check(child.name !in children) { "Child with name ${child.name} already exists" }
        children[child.name] = child
        child.parent = this
    }

    fun removeChild(childId: String) {
        children.remove(childId)
    }

    fun getNode(path: String): Node? {
        val parts = path.split("/")
        var current: Node = this

        for (part in parts)
            current = current.children[part] ?: return null

        return current
    }

    // --- Lifecycle methods triggered by SceneTree ---
    internal fun enterTree() {
        script?.onEnterTree()
        children.values.forEach { it.enterTree() }
    }

    internal fun ready() {
        children.values.forEach { it.ready() }
        script?.onReady()
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


@Suppress("ktlint:standard:function-naming")
fun NodeBuilder.Node2D(
    name: String,
    script: Behavior.Factory<Node, Behavior<Node>>? = null,
    position: Vector2 = Vector2(0f, 0f),
    scale: Vector2 = Vector2(0f, 0f),
    rotation: Float = 0f,
    block: NodeBuilder.() -> Unit = {},
) = add(
    Node(
        name = name,
        factory = script,
        position = position,
        scale = scale,
        rotation = rotation,
    ),
    block,
)
