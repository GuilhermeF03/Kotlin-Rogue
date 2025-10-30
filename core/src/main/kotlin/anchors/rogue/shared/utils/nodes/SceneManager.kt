package anchors.rogue.shared.utils.nodes

import anchors.rogue.shared.ecs.managers.Manager

typealias GlobalSystem = Behavior<Node>

class SceneTreeManager : Manager() {
    lateinit var currScene: Node
    private val systems: MutableList<GlobalSystem> = mutableListOf()

    fun registerSystem(system: GlobalSystem) {
        systems += system
    }

    fun unregisterSystem(system: GlobalSystem) {
        systems -= system
    }

    fun ready(){

    }

}
