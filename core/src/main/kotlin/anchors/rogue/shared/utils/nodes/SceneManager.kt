package anchors.rogue.shared.utils.nodes

import anchors.rogue.shared.managers.Manager
import anchors.rogue.shared.utils.input.InputEvent
import anchors.rogue.shared.utils.nodes.core.GlobalNodeSystem
import anchors.rogue.shared.utils.nodes.core.Node
import anchors.rogue.shared.utils.nodes.core.UpdatePhase
import anchors.rogue.shared.utils.nodes.types.camera.Camera2D
import anchors.rogue.shared.utils.signals.createSignal
import ktx.log.logger
import kotlin.reflect.KClass

/**
 * SceneManager is responsible for managing a scene tree, systems, groups, and active camera.
 *
 * Responsibilities:
 * - Scene replacement and lifecycle
 * - Fixed-step physics and frame updates
 * - Global systems execution
 * - Node grouping and signaling
 * - Active camera tracking and resize notifications
 */
class SceneManager(
    private var physicsStep: Float = 1f / 60f,
    block: SceneManager.() -> Unit = {},
) : Manager {
    private val logger = logger<SceneManager>()

    // ==========================
    //          CAMERA
    // ==========================
    var activeCamera: Camera2D? = null
        private set

    /** Emitted whenever the active camera changes */
    val onCameraChanged = createSignal<Camera2D?>()

    internal fun registerCamera(cam: Camera2D) {
        if (activeCamera == cam) return
        activeCamera = cam
        logger.debug { "Active camera set to ${cam.name}" }
        onCameraChanged.emit(cam)
    }

    internal fun unregisterCamera(cam: Camera2D) {
        if (activeCamera == cam) {
            logger.debug { "Camera ${cam.name} unregistered as active camera" }
            activeCamera = null
            onCameraChanged.emit(null)
        }
    }

    // ===============================
    //         SIGNALS
    // ===============================

    /** Emitted when the window or viewport is resized */
    val onResize = createSignal<Int, Int>()

    // ===============================
    //         SCENE STATE
    // ===============================
    private var _currScene: Node<*>? = null
    var currScene: Node<*>?
        get() = _currScene
        set(value) = replaceScene(value)

    private var physicsAccumulator = 0f

    // ===============================
    //         SYSTEMS
    // ===============================
    private val systems: Map<UpdatePhase, MutableList<GlobalNodeSystem>> =
        mapOf(
            UpdatePhase.Input to mutableListOf(),
            UpdatePhase.FrameBeforeScene to mutableListOf(),
            UpdatePhase.FrameAfterScene to mutableListOf(),
            UpdatePhase.PhysicsBeforeScene to mutableListOf(),
            UpdatePhase.PhysicsAfterScene to mutableListOf(),
        )

    private val systemsByType = mutableMapOf<KClass<out Node<*>>, MutableList<GlobalNodeSystem>>()

    // ===============================
    //          GROUPS
    // ===============================
    val groups: MutableMap<String, MutableList<Node<*>>> = mutableMapOf()

    init {
        block(this)
    }

    // ===============================
    //      SCENE MANAGEMENT
    // ===============================
    private fun replaceScene(newScene: Node<*>?) {
        val oldScene = _currScene

        oldScene?.let {
            it.exitTree()
            unregisterSubtree(it)
        }

        _currScene = newScene
        activeCamera = null // Reset camera on scene change

        newScene?.let {
            registerSubtree(it)
            it.buildTree()
        }
    }

    internal fun registerSubtree(root: Node<*>? = currScene) {
        root ?: return
        traverseNodes(root) { node ->
            systemsByType[node::class]?.forEach { sys -> sys.register(node) }
        }
    }

    internal fun unregisterSubtree(root: Node<*>? = currScene) {
        root ?: return
        traverseNodes(root) { node ->
            systemsByType[node::class]?.forEach { sys -> sys.unregister(node) }
        }
    }

    private fun traverseNodes(
        node: Node<*>,
        action: (Node<*>) -> Unit,
    ) {
        action(node)
        node.children.values.forEach { traverseNodes(it, action) }
    }

    // ===============================
    //      SYSTEM MANAGEMENT
    // ===============================
    fun addSystem(system: GlobalNodeSystem) {
        systems[system.phase]?.add(system)
        system.requiredTypes.forEach { type ->
            systemsByType.computeIfAbsent(type) { mutableListOf() }.add(system)
        }
    }

    fun removeSystem(system: GlobalNodeSystem) {
        systems[system.phase]?.remove(system)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : GlobalNodeSystem> getSystem(clazz: KClass<T>): T =
        systems.values.flatten().firstOrNull { clazz.isInstance(it) } as T

    // ===============================
    //      GROUP MANAGEMENT
    // ===============================
    fun addToGroup(
        group: String,
        node: Node<*>,
    ) {
        val groupNodes = groups.computeIfAbsent(group) { mutableListOf() }
        groupNodes += node
    }

    fun removeFromGroup(
        group: String,
        node: Node<*>,
    ) {
        val groupNodes = groups[group] ?: error("Group $group does not exist")
        groupNodes -= node
    }

    fun signalGroup(
        group: String,
        callback: (node: Node<*>) -> Unit,
    ) {
        val groupNodes = groups[group] ?: error("Group $group does not exist")
        groupNodes.forEach(callback)
    }

    // ===============================
    //             TICK
    // ===============================
    fun tick(delta: Float) {
        val root = currScene ?: return
        systems[UpdatePhase.Input]?.forEach { it.tick(delta) }

        if (isPhysicsFrame(delta)) {
            systems[UpdatePhase.PhysicsBeforeScene]?.forEach { it.tick(physicsStep) }
            root.physicsUpdate(physicsStep)
            systems[UpdatePhase.PhysicsAfterScene]?.forEach { it.tick(physicsStep) }
        }

        systems[UpdatePhase.FrameBeforeScene]?.forEach { it.tick(delta) }
        root.update(delta)
        systems[UpdatePhase.FrameAfterScene]?.forEach { it.tick(delta) }
    }

    fun resize(
        width: Int,
        height: Int,
    ) {
        onResize.emit(width, height)
    }

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

    // ===============================
    //             INPUT
    // ===============================
    fun onInput(
        event: InputEvent,
        delta: Float,
    ) {
        currScene?.input(event, delta)
    }
}
