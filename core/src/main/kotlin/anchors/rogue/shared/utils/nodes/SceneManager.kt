package anchors.rogue.shared.utils.nodes

import anchors.rogue.shared.ecs.managers.Manager

class SceneManager : Manager() {
    /**
     * Root of current scene
     */
    var currScene: Node? = null

    /**
     * List of global systems
     */
    private val systems: MutableList<GlobalNodeSystem> = mutableListOf()

    fun registerSystem(system: GlobalNodeSystem) {
        systems += system
    }

    fun unregisterSystem(system: GlobalNodeSystem) {
        systems -= system
    }

    fun ready() {
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
