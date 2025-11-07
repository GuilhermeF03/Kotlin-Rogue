package anchors.rogue.shared.utils.nodes.core

import anchors.rogue.shared.utils.input.InputEvent

/**
 * Represents a custom behavior that can be attached to a Node
 * @see anchors.rogue.shared.utils.nodes.Node
 */
abstract class Behavior<N : Node<N>>(
    /**
     * Holds reference for the node the behavior is attached to.
     */
    private val node: N? = null,
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

//    interface Factory<T : Node, B : Behavior<T>> {
//        fun create(node: T): B
//    }
}

/**
 * Helper method for creating lambda behaviors
 */
fun <N : Node<N>> behavior(
    onEnterTree: N.() -> Unit = {},
    onReady: N.() -> Unit = {},
    onExitTree: N.() -> Unit = {},
    onUpdate: N.(delta: Float) -> Unit = {},
    onPhysicsUpdate: N.(delta: Float) -> Unit = {}
) : (node : N) -> Behavior<N> = { node ->
    object : Behavior<N>(node) {
        override fun onEnterTree() = super.onEnterTree().also { onEnterTree(node) }
        override fun onReady() = super.onReady().also { onReady(node) }
        override fun onExitTree() = super.onExitTree().also { onExitTree(node) }
        override fun onUpdate(delta: Float) = super.onUpdate(delta).also { onUpdate(node, delta) }
        override fun onPhysicsUpdate(delta: Float) = super.onPhysicsUpdate(delta).also {
            onPhysicsUpdate(node, delta)
        }
    }
}
