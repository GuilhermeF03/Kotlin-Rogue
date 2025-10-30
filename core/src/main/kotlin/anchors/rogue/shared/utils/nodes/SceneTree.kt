package anchors.rogue.shared.utils.nodes


typealias GlobalSystem = Behavior<Node>
data class SceneTree(
    val root : Node
){
    private val systems : MutableList<GlobalSystem> = mutableListOf()

    // Lifecycle lists
    private val processList : MutableList<Node> = mutableListOf()
    private val physicsProcessList : MutableList<Node> = mutableListOf()

    fun registerSystem(system : GlobalSystem){
        systems += system
    }

    fun unregisterSystem(system : GlobalSystem){
        systems -= system
    }

    fun markForProcess(node : Node){
        processList += node
    }

    fun markForPhysicsProcess(node : Node){
        physicsProcessList += node
    }



}

