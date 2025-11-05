package anchors.rogue.shared.utils.nodes

import anchors.rogue.shared.utils.input.InputEvent

/**
 * Represents a custom behavior that can be attached to a Node
 * @see Node
 */
abstract class Behavior<T : Node>(
    /**
     * Holds reference for the node the behavior is attached to.
     */
    private val node: T? = null,
) {
    constructor() : this(null)

    /**
     * Method called when node enters tree
     */
    open fun onEnterTree() = Unit

    /**
     * Method called after node entered tree, and all its children have been set up
     */
    open fun onReady() = Unit

    /**
     * Method called when tree exits tree
     */
    open fun onExitTree() = Unit

    /**
     * Normal update method - time between calls is based on machine frame rate
     *
     * Useful for rendering and operations where frame rate stability is not important
     */
    open fun onUpdate(delta: Float) = Unit

    /**
     * Physics update method - time between calls is stable
     *
     * Useful for physics and operations where frame rate stability matters
     */
    open fun onPhysicsUpdate(delta: Float) = Unit

    /**
     * Called on an executed input event
     */
    open fun onInput(event : InputEvent) = Unit

    interface Factory<T : Node, B : Behavior<T>> {
        fun create(node: T): B
    }
}

/**
 * Helper method for creating lambda behaviors
 */
fun <T : Node> behavior(
    onEnterTree: T.() -> Unit = {},
    onReady: T.() -> Unit = {},
    onExitTree: T.() -> Unit = {},
    onUpdate: T.(delta: Float) -> Unit = {},
    onPhysicsUpdate: T.(delta: Float) -> Unit = {}
) = object : Behavior.Factory<T, Behavior<T>> {
        override fun create(node: T): Behavior<T> = object : Behavior<T>() {
            override fun onEnterTree() = super.onEnterTree().also { onEnterTree(node) }
            override fun onReady() = super.onReady().also { onReady(node) }
            override fun onExitTree() = super.onExitTree().also { onExitTree(node) }
            override fun onUpdate(delta: Float) = super.onUpdate(delta).also { onUpdate(node,delta) }
            override fun onPhysicsUpdate(delta: Float) = super.onPhysicsUpdate(delta).also { onPhysicsUpdate(node,delta) }
        }
    }
