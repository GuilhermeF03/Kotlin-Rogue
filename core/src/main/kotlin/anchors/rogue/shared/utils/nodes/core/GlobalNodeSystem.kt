package anchors.rogue.shared.utils.nodes.core

import anchors.rogue.shared.utils.nodes.types.empty.EmptyNode
import kotlin.reflect.KClass

/**
 *
 */
enum class UpdatePhase {
    PhysicsBeforeScene,
    PhysicsAfterScene,
    FrameBeforeScene,
    FrameAfterScene
}

/**
 *
 */
abstract class GlobalNodeSystem(
    val phase : UpdatePhase,
    vararg val requiredTypes: KClass<out Node<*>>
) {

    /**
     *
     */
    protected val matchingNodes = mutableListOf<Node<*>>()

    /**
     * Called once before updates begin
     */
    open fun onSystemInit() {}

    /**
     *
     */
    open fun onSystemClose(){}

    /**
     * Tick executed by scheduler
     */
    fun tick(delta: Float) {
        beforeProcess(delta)
        matchingNodes.forEach { processNode(it, delta) }
        afterProcess(delta)
    }

    /**
     * Lifecycle hooks around iteration (optional)
     */
    protected open fun beforeProcess(delta: Float) {}
    protected open fun afterProcess(delta: Float) {}

    /**
     * User override → actual logic applied per node
     */
    protected open fun processNode(node: Node<*>, delta: Float){}

    /**
     * Node registry hooks — called by tree lifecycle
     */
    fun register(node: Node<*>) {
        if (acceptsNode(node)) {
            matchingNodes += node
            onNodeAdded(node)
        }
    }

    fun unregister(node: Node<*>) {
        if (matchingNodes.remove(node)) {
            onNodeRemoved(node)
        }
    }

    protected open fun onNodeAdded(node: Node<*>) {}
    protected open fun onNodeRemoved(node: Node<*>) {}

    private fun acceptsNode(node: Node<*>) =
        requiredTypes.all { type ->
            type.isInstance(node) || node.hasChildType(type)
        }
}

