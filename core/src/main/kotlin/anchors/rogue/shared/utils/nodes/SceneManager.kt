package anchors.rogue.shared.utils.nodes

import anchors.rogue.shared.ecs.managers.Manager
import anchors.rogue.shared.utils.nodes.core.GlobalNodeSystem
import anchors.rogue.shared.utils.nodes.core.Node
import anchors.rogue.shared.utils.nodes.core.UpdatePhase
import kotlin.collections.plusAssign
import kotlin.reflect.KClass


class SceneManager(
    private var physicsStep : Float = 1F / 60F, // Default physics step
    block : SceneManager.() -> Unit = {}
) : Manager() {

    init { block(this) }

    /**
     * Root of current scene
     */
    private var _currScene : Node<*>? = null

    var currScene : Node<*>?
        get() = _currScene
        set(value) = replaceScene(value)

    /**
     *
     */
    private var physicsAccumulator = 0F

    /**
     * Register a global system
     */
    fun addSystem(system: GlobalNodeSystem) {
        systems[system.phase]?.add(system)

        // Index by required types
        system.requiredTypes.forEach { type ->
            systemsByType.computeIfAbsent(type) { mutableListOf() }.add(system)
        }
    }

    /**
     * Unregister a global system
     */
    fun removeSystem(system: GlobalNodeSystem) {
        systems[system.phase]?.remove(system)
    }

    /**
     * Called when a node (or subtree) enters the tree
     */
    internal fun registerSubtree(root: Node<*>? = currScene) {
        if(root == null) return
        traverseNodes(root) { node ->
            val typeMatches = systemsByType[node::class]
            typeMatches?.forEach { sys ->
                sys.register(node)
            }
        }
    }

    /**
     * Called when a node (or subtree) exits the tree
     */
    internal fun unregisterSubtree(root: Node<*>? = currScene) {
        if(root == null) return
        traverseNodes(root) { node ->
            val typeMatches = systemsByType[node::class]
            typeMatches?.forEach { sys ->
                sys.unregister(node)
            }
        }
    }

    /**
     *
     */
    private fun traverseNodes(node: Node<*>, action: (Node<*>) -> Unit) {
        action(node)
        node.children.values.forEach { child -> traverseNodes(child, action) }
    }

    /**
     *
     */
    fun tick(delta: Float) {
        val state = currScene ?: return

        // Fixed physics step
       if(isPhysicsFrame(delta)) {
            systems[UpdatePhase.PhysicsBeforeScene]?.forEach { it.tick(physicsStep) }
            state.physicsUpdate(physicsStep)
            systems[UpdatePhase.PhysicsAfterScene]?.forEach { it.tick(physicsStep) }
        }

        // Variable update
        systems[UpdatePhase.FrameBeforeScene]?.forEach { it.tick(delta) }
        state.update(delta)
        systems[UpdatePhase.FrameAfterScene]?.forEach { it.tick(delta) }
    }

    /**
     *
     */
    private fun replaceScene(newScene : Node<*>?){
        val treeState = _currScene
        // Clean old tree
        if(treeState != null){
            treeState.exitTree()
            unregisterSubtree(treeState)
        }

        _currScene = newScene

        // Set up new tree
        if(newScene != null){
            registerSubtree(newScene)
            newScene.buildTree()
        }
    }

    /**
     *
     */
    private fun isPhysicsFrame(delta : Float) : Boolean {
        physicsAccumulator += delta
        if(physicsAccumulator >= physicsStep) return true.also { physicsAccumulator -= physicsStep }
        return false
    }

    override fun setup() {
        systems.values.fold(listOf<GlobalNodeSystem>()){ prev, next ->
            prev + next
        }.forEach(GlobalNodeSystem::onSystemInit)
    }

    override fun teardown() {
        systems.values.fold(listOf<GlobalNodeSystem>()){ prev, next ->
            prev + next
        }.forEach(GlobalNodeSystem::onSystemClose)
    }

    companion object{
        /**
         *
         */
        private val systems = mapOf<UpdatePhase, MutableList<GlobalNodeSystem>>(
            UpdatePhase.FrameBeforeScene to mutableListOf(),
            UpdatePhase.FrameAfterScene to mutableListOf(),
            UpdatePhase.PhysicsBeforeScene to mutableListOf(),
            UpdatePhase.PhysicsAfterScene to mutableListOf()
        )

        // Optimization: map types → systems requiring them
        private val systemsByType = mutableMapOf<KClass<out Node<*>>, MutableList<GlobalNodeSystem>>()
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
