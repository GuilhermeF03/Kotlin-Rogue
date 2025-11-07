package anchors.rogue.shared.utils.nodes.core

import anchors.rogue.shared.ecs.managers.ManagersRegistry
import anchors.rogue.shared.utils.input.InputEvent
import anchors.rogue.shared.utils.nodes.SceneManager
import anchors.rogue.shared.utils.nodes.core.Behavior
import com.badlogic.gdx.math.Vector2
import ktx.math.plus
import kotlin.reflect.KClass

@DslMarker
annotation class SceneDSL

@SceneDSL
@Suppress("UNCHECKED_CAST")
abstract class Node<N : Node<N>> internal constructor(
    /**
     * Node name
     */
    val name: String,
    script: (node : N) -> Behavior<N>? = { null },
    /**
     * Node position in 2D coordinates
     */
    var position: Vector2 = Vector2.Zero,
    /**
     * Node scale, in 2D space
     */
    var scale: Vector2 = Vector2(1f, 1f),
    /**
     * Node rotation in rads
     */
    var rotation: Float = 0f,
    block: Node<*>.() -> Unit = {},
) {
    /**
     * Reference to scene manager
     */
    internal val manager : SceneManager = ManagersRegistry.get(SceneManager::class)

    /**
     * Script for custom behavior
     */
    private val script : Behavior<N>? = script(this as N)

    private var _parent: Node<*>? = null

    /**
     * Node parent
     */
    val parent get() = _parent

    private val _children: MutableMap<String, Node<*>> = mutableMapOf()

    /**
     * Node children
     */
    val children : Map<String, Node<*>> get() = _children

    val globalPosition: Vector2
        get() = position + (parent?.globalPosition ?: Vector2.Zero)

    val globalScale : Vector2
        get() = scale + (parent?.globalScale ?: Vector2.Zero)

    val globalRotation: Float
        get() = rotation + (parent?.globalRotation ?: 0f)

    // DSL Helpers - allows structures to be built by using class constructors
    companion object {
        private val currentParent = ThreadLocal.withInitial<Node<*>?> { null }
    }

    init {
        val parent = currentParent.get()
        parent?.addChildInternal(this)

        val oldParent = currentParent.get()
        currentParent.set(this)

        this.block()

        currentParent.set(oldParent)
    }

    // ==================================================
    //                  NODE OPERATIONS
    // ==================================================

    private fun addChildInternal(child: Node<*>) {
        check(child.name !in children) { "Child with name '${child.name}' already exists" }
        _children[child.name] = child
        child._parent = this
    }

    /**
     * Add child node
     */
    fun addChild(child: Node<*>) {
        check(child.parent == null) { "Node '${child.name}' already has a parent!" }
        addChildInternal(child)

        // Runtime attach lifecycle
        child.enterTree()
        child.ready()

        manager.registerSubtree(this)
    }

    /**
     * Gets node, based on provided path
     */
    fun getNode(path: String): Node<*>? {
        val parts = path.split("/")
        var current: Node<*> = this
        for (part in parts) {
            current = current.children[part] ?: return null
        }
        return current
    }

    /**
     * Remove child node
     */
    fun removeChild(child: Node<*>) {
        check(child.parent == this) { "Node '${child.name}' is not a child of '${name}'!" }

        // Call lifecycle teardown before removal
        child.exitTree()

        _children.remove(child.name)
        child._parent = null

        manager.unregisterSubtree(this)
    }

    /**
     * Remove child through its path
     */
    fun removeChild(path: String) {
        val child = getNode(path)
        checkNotNull(child) { "Node at path $path not found" }
        removeChild(child)
    }

    /**
     * Self remove node from tree
     */
    fun queueFree() {
        val parent = parent ?: return
        parent.removeChild(this)
    }

    /**
     * Reparents nodes on another node inside the tree
     */
    fun reparent(child : Node<*>, newParent : Node<*>){
        removeChild(child)
        newParent.addChild(child)
    }

    // ================================
    //        Lifecycle operations
    // ================================

    /**
     * Called after the initial tree structure was defined - calls setup methods
     */
    fun buildTree() {
        enterTree() // top→down attach event
        ready() // bottom→up initialization
    }

    // ===================
    //  Lifecycle methods
    // ===================

    /**
     * Method called after node entered tree, and all its children have been set up
     */
    open fun ready() {
        children.values.forEach { it.ready() }
        script?.onReady()
    }

    /**
     * Method called when node enters tree
     */
    open fun enterTree() {

        script?.onEnterTree()
        children.values.forEach { it.enterTree() }
    }

    /**
     * Method called when tree exits tree
     */
    open fun exitTree() {
        children.values.forEach { it.exitTree() }
        script?.onExitTree()
    }

    /**
     * Normal update method - time between calls is based on machine frame rate
     *
     * Useful for rendering and operations where frame rate stability is not important
     */
    open fun update(delta: Float) {
        children.values.forEach { it.update(delta) }
        script?.onUpdate(delta)
    }

    /**
     * Physics update method - time between calls is stable
     *
     * Useful for physics and operations where frame rate stability matters
     */
    open fun physicsUpdate(delta: Float) {
        children.values.forEach { it.physicsUpdate(delta) }
        script?.onPhysicsUpdate(delta)
    }

    /**
     * Called on an executed input event
     */
    open fun input(event : InputEvent){
        if(event.isHandled) return
        script?.onInput(event)

        if(event.isHandled) return

        // Propagate
        children.values.forEach { it.input(event)}
    }

    fun hasChildType(type: KClass<out Node<*>>) =
        children.values.any { it::class == type }
}
