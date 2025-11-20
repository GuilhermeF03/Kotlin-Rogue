package anchors.rogue.shared.utils.nodes.core

import anchors.rogue.shared.managers.ManagersRegistry
import anchors.rogue.shared.utils.input.InputEvent
import anchors.rogue.shared.utils.input.InputSystem
import anchors.rogue.shared.utils.nodes.SceneManager
import com.badlogic.gdx.math.Vector2
import ktx.log.logger
import ktx.math.plus
import kotlin.reflect.KClass

/**
 * DSL marker for Scene DSL usage.
 */
@DslMarker
annotation class NodeDSL

/**
 * Base node class for 2D scene graph system.
 *
 * @param N Type of the node (for generics / DSL chaining)
 */
@NodeDSL
@Suppress("UNCHECKED_CAST")
abstract class Node<N : Node<N>> internal constructor(
    // ===============================
    //            CORE PROPERTIES
    // ===============================
    /** Node name (unique among siblings) */
    val name: String,
    /** Optional behavior script attached to the node */
    script: (node: N) -> Behavior<N>?,
    /** Local position in 2D space */
    open var position: Vector2 = Vector2.Zero,
    /** Local scale in 2D space */
    open var scale: Vector2 = Vector2(1f, 1f),
    /** Local rotation in radians */
    open var rotation: Float = 0f,
    /** Optional DSL block for building children inline */
    block: Node<*>.() -> Unit,
) {
    // ===============================
    //        INTERNAL PROPERTIES
    // ===============================
    /** Reference to the scene manager (set automatically on init) */
    internal val sceneManager: SceneManager by lazy { ManagersRegistry.get(SceneManager::class) }
    internal val inputManager: InputSystem by lazy { sceneManager.getSystem(InputSystem::class) }

    /** Whether this node is a prefab (not active until instantiated) */
    private var isPrefab: Boolean = false

    /** Reference to the scene manager */
    internal val manager: SceneManager = ManagersRegistry.get(SceneManager::class)

    /** Groups this node belongs to */
    private val groups: MutableList<String> = mutableListOf()

    /** Behavior script instance */
    private val script: Behavior<N>? = script(this as N)

    /** Parent node reference */
    private var _parent: Node<*>? = null
    val parent get() = _parent

    /** Child nodes mapped by name */
    private val _children: MutableMap<String, Node<*>> = mutableMapOf()
    val children: Map<String, Node<*>> get() = _children

    // ===============================
    //      GLOBAL TRANSFORMS
    // ===============================

    /** Global position (includes parent transforms) */
    val globalPosition: Vector2
        get() = position + (parent?.globalPosition ?: Vector2.Zero)

    /** Global scale (includes parent transforms) */
    val globalScale: Vector2
        get() = scale + (parent?.globalScale ?: Vector2.Zero)

    /** Global rotation (includes parent rotation) */
    val globalRotation: Float
        get() = rotation + (parent?.globalRotation ?: 0f)

    // ===============================
    //           DSL SUPPORT
    // ===============================
    companion object {
        /** Tracks the current parent for building the tree via DSL */
        private val currentParent = ThreadLocal.withInitial<Node<*>?> { null }
    }

    init {
        // Attach to current DSL parent if exists
        currentParent.get()?.addChildInternal(this)

        val oldParent = currentParent.get()
        currentParent.set(this)
        this.block()
        currentParent.set(oldParent)
    }

    // ===============================
    //          CHILD MANAGEMENT
    // ===============================

    private fun addChildInternal(child: Node<*>) {
        check(child.name !in children) { "Child with name '${child.name}' already exists" }
        _children[child.name] = child
        child._parent = this
        manager.registerSubtree(child)
    }

    /** Adds a child node at runtime */
    fun addChild(child: Node<*>) {
        check(child.parent == null) { "Node '${child.name}' already has a parent!" }
        addChildInternal(child)

        if (child.isPrefab) return

        // Lifecycle setup for runtime node
        child.enterTree()
        child.ready()
    }

    /** Removes a child node */
    fun removeChild(child: Node<*>) {
        check(child.parent == this) { "Node '${child.name}' is not a child of '$name'!" }

        // Lifecycle teardown
        child.exitTree()
        _children.remove(child.name)
        child._parent = null

        manager.unregisterSubtree(this)
    }

    /** Removes a child node by path */
    fun removeChild(path: String) {
        val child = getNode(path)
        checkNotNull(child) { "Node at path $path not found" }
        removeChild(child)
    }

    /** Returns a child node by relative path (e.g., "parent/child") */
    fun <T : Node<T>> getNode(path: String): T? {
        var current: Node<*> = this
        for (part in path.split("/")) {
            current = current.children[part] ?: return null
        }
        return current as? T
    }

    /** Marks this node as a prefab (not active until instantiated) */
    fun asPrefab(): N {
        isPrefab = true
        return this as N
    }

    /** Self-remove from parent */
    fun queueFree() {
        parent?.removeChild(this)
    }

    /** Reparent a child to another node */
    fun reparent(
        child: Node<*>,
        newParent: Node<*>,
    ) {
        removeChild(child)
        newParent.addChild(child)
    }

    /** Checks if any child is of a given type */
    fun hasChildType(type: KClass<out Node<*>>) = children.values.any { it::class == type }

    // ===============================
    //         GROUP MANAGEMENT
    // ===============================

    /** Checks if the node is in a group */
    fun inGroup(group: String) = group in groups

    /** Adds the node to a group */
    fun addGroup(group: String) = groups.add(group).also { manager.addToGroup(group, this) }

    /** Removes the node from a group */
    fun removeGroup(group: String) = groups.remove(group).also { manager.removeFromGroup(group, this) }

    // ===============================
    //       SCENE TREE BUILDING
    // ===============================

    /** Builds the tree and calls lifecycle methods */
    fun buildTree() {
        enterTree() // top-down attach
        ready() // bottom-up initialization
    }

    // ===============================
    //        LIFECYCLE METHODS
    // ===============================

    /** Called after the node and its children are fully initialized */
    open fun ready() {
        children.values.forEach { it.ready() }
        script?.onReady()
    }

    /** Called when node enters the tree */
    open fun enterTree() {
        script?.onEnterTree()
        children.values.forEach { it.enterTree() }
    }

    /** Called when node exits the tree */
    open fun exitTree() {
        children.values.forEach { it.exitTree() }
        script?.onExitTree()
    }

    // ===============================
    //          UPDATES
    // ===============================

    /** Called every frame */
    open fun update(delta: Float) {
        children.values.forEach { it.update(delta) }
        script?.onUpdate(delta)
    }

    /** Called every physics tick */
    open fun physicsUpdate(delta: Float) {
        children.values.forEach { it.physicsUpdate(delta) }
        script?.onPhysicsUpdate(delta)
    }

    // ===============================
    //           INPUT
    // ===============================

    /** Handles input events and propagates to children */
    open fun input(
        event: InputEvent,
        delta: Float = 0F,
    ) {
        if (event.isHandled) return
        script?.onInput(event, delta)
        if (event.isHandled) return
        // Propagate ito children
        children.values.forEach { it.input(event, delta) }
    }
}
