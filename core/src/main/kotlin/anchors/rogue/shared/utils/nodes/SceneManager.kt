package anchors.rogue.shared.utils.nodes

import anchors.rogue.shared.managers.Manager
import anchors.rogue.shared.utils.input.InputEvent
import anchors.rogue.shared.utils.nodes.core.GlobalNodeSystem
import anchors.rogue.shared.utils.nodes.core.Node
import anchors.rogue.shared.utils.nodes.core.UpdatePhase
import anchors.rogue.shared.utils.nodes.types.camera.Camera2D
import anchors.rogue.shared.utils.signals.createSignal
import kotlin.reflect.KClass

/**
 * SceneManager is responsible for managing a scene tree, global systems, and groups.
 *
 * It handles:
 * - Scene replacement and lifecycle
 * - Fixed-step physics updates
 * - Global node systems execution
 * - Node grouping and signals
 */
class SceneManager(
    private var physicsStep: Float = 1F / 60F, // Default physics step
    block: SceneManager.() -> Unit = {},
) : Manager {
    // ==========================
    //          CAMERA
    // ==========================
    var activeCamera: Camera2D? = null
        private set

    internal fun registerCamera(cam: Camera2D) {
        activeCamera = cam
    }

    internal fun unregisterCamera(cam: Camera2D) {
        if (activeCamera == cam) activeCamera = null
    }

    // ===============================
    //         SIGNALS
    // ===============================

    /** Emitted when the window or viewport is resized */
    val onResize = createSignal<Int, Int>()

    // ===============================
    //       SCENE & TREE STATE
    // ===============================

    /** Current root scene */
    private var _currScene: Node<*>? = null

    /** Public accessor for the current scene; automatically replaces scene on set */
    var currScene: Node<*>?
        get() = _currScene
        set(value) = replaceScene(value)

    /** Accumulator for fixed-step physics */
    private var physicsAccumulator = 0F

    // ===============================
    //           SYSTEMS
    // ===============================

    /** All global systems organized by update phase */
    private val systems: Map<UpdatePhase, MutableList<GlobalNodeSystem>> =
        mapOf(
            UpdatePhase.Input to mutableListOf(),
            UpdatePhase.FrameBeforeScene to mutableListOf(),
            UpdatePhase.FrameAfterScene to mutableListOf(),
            UpdatePhase.PhysicsBeforeScene to mutableListOf(),
            UpdatePhase.PhysicsAfterScene to mutableListOf(),
        )

    /** Maps node types to systems requiring them for faster registration */
    private val systemsByType = mutableMapOf<KClass<out Node<*>>, MutableList<GlobalNodeSystem>>()

    // ===============================
    //            GROUPS
    // ===============================

    /** Tracks nodes belonging to groups */
    val groups: MutableMap<String, MutableList<Node<*>>> = mutableMapOf()

    // ===============================
    //            INIT
    // ===============================

    init {
        block(this)
    }

    // ===============================
    //        SCENE MANAGEMENT
    // ===============================

    /** Replace the current scene with a new root node */
    private fun replaceScene(newScene: Node<*>?) {
        val oldScene = _currScene

        // Clean old tree
        oldScene?.let {
            it.exitTree()
            unregisterSubtree(it)
        }

        _currScene = newScene

        // Set up new tree
        newScene?.let {
            registerSubtree(it)
            it.buildTree()
        }
    }

    /**
     * Register a node and all its children to appropriate systems
     * @param root Node subtree root. Defaults to the current scene root.
     */
    internal fun registerSubtree(root: Node<*>? = currScene) {
        root ?: return
        traverseNodes(root) { node ->
            systemsByType[node::class]?.forEach { sys -> sys.register(node) }
        }
    }

    /**
     * Unregister a node and all its children from systems
     * @param root Node subtree root. Defaults to the current scene root.
     */
    internal fun unregisterSubtree(root: Node<*>? = currScene) {
        root ?: return
        traverseNodes(root) { node ->
            systemsByType[node::class]?.forEach { sys -> sys.unregister(node) }
        }
    }

    /** Helper to traverse all nodes recursively */
    private fun traverseNodes(
        node: Node<*>,
        action: (Node<*>) -> Unit,
    ) {
        action(node)
        node.children.values.forEach { traverseNodes(it, action) }
    }

    // ===============================
    //       SYSTEM MANAGEMENT
    // ===============================

    /** Register a global system */
    fun addSystem(system: GlobalNodeSystem) {
        systems[system.phase]?.add(system)
        system.requiredTypes.forEach { type ->
            systemsByType.computeIfAbsent(type) { mutableListOf() }.add(system)
        }
    }

    /** Remove a global system */
    fun removeSystem(system: GlobalNodeSystem) {
        systems[system.phase]?.remove(system)
    }

    // ===============================
    //         GROUP MANAGEMENT
    // ===============================

    /** Add a node to a group */
    fun addToGroup(
        group: String,
        node: Node<*>,
    ) {
        val groupNodes = groups.computeIfAbsent(group) { mutableListOf() }
        groupNodes += node
    }

    /** Remove a node from a group */
    fun removeFromGroup(
        group: String,
        node: Node<*>,
    ) {
        val groupNodes = groups[group] ?: error("Group $group does not exist")
        groupNodes -= node
    }

    /** Invoke a callback for all nodes in a group */
    fun signalGroup(
        group: String,
        callback: (node: Node<*>) -> Unit,
    ) {
        val groupNodes = groups[group] ?: error("Group $group does not exist")
        groupNodes.forEach(callback)
    }

    // ===============================
    //            TICK
    // ===============================

    /**
     * Update the scene tree and all global systems.
     * Handles fixed-step physics and variable frame updates.
     */
    fun tick(delta: Float) {
        val root = currScene ?: return

        systems[UpdatePhase.Input]?.forEach { it.tick(delta) }

        // Fixed-step physics
        if (isPhysicsFrame(delta)) {
            systems[UpdatePhase.PhysicsBeforeScene]?.forEach { it.tick(physicsStep) }
            root.physicsUpdate(physicsStep)
            systems[UpdatePhase.PhysicsAfterScene]?.forEach { it.tick(physicsStep) }
        }

        // Variable frame updates
        systems[UpdatePhase.FrameBeforeScene]?.forEach { it.tick(delta) }
        root.update(delta)
        systems[UpdatePhase.FrameAfterScene]?.forEach { it.tick(delta) }
    }

    /** Handle window resize */
    fun resize(
        width: Int,
        height: Int,
    ) {
        onResize.emit(width, height)
    }

    /** Check if physics should run this frame */
    private fun isPhysicsFrame(delta: Float): Boolean {
        physicsAccumulator += delta
        if (physicsAccumulator >= physicsStep) {
            physicsAccumulator -= physicsStep
            return true
        }
        return false
    }

    // ===============================
    //        LIFECYCLE HOOKS
    // ===============================

    override fun setup() {
        systems.values.flatten().forEach(GlobalNodeSystem::onSystemInit)
    }

    override fun teardown() {
        systems.values.flatten().forEach(GlobalNodeSystem::onSystemClose)
    }

    // =================================
    //             INPUT
    // =================================
    fun onInput(event: InputEvent) {
        val root = currScene ?: return
        root.input(event) // Dispatch input through tree
    }
}
