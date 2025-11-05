package anchors.rogue.shared.utils.nodes

import anchors.rogue.shared.ecs.managers.Manager

class SceneManager(
    private var physicsStep : Float = 1F / 60F // Default physics step
) : Manager() {

    private var tree : Node? = null

    /**
     * Root of current scene
     */
    val currScene: Node? get() = tree

    private var physicsAccumulator = 0F

    /**
     * List of global systems
     */
    private val systems: MutableList<GlobalNodeSystem> = mutableListOf()

    /**
     * Register a global system
     */
    fun registerSystem(system: GlobalNodeSystem) {
        systems += system
    }

    /**
     * Unregister a global system
     */
    fun unregisterSystem(system: GlobalNodeSystem) {
        systems -= system
    }

    fun tick(delta : Float){
        val treeState = tree ?: return

        physicsAccumulator += delta
        while (physicsAccumulator >= physicsStep) {
            tree?.physicsUpdate(physicsStep)
            physicsAccumulator -= physicsStep
        }
        treeState.update(delta)
    }
}

/**
 * GOAL:
 *
 * [ktx.app.KtxScreen]
 *
 * val scene = scene(EmptyNode()){
 *      PlayerScene()
 * }
 *
 * SceneManager.currScene = scene <- clean old tree, initialize new tree, sets up lifecycle methods,
 * registers lists for global systems
 *
 */
