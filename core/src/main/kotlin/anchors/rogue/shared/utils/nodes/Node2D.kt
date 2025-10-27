package anchors.rogue.shared.utils.nodes

import com.badlogic.gdx.math.Vector2
import ktx.math.plus

open class Node2D internal constructor(
    val name: String,
    val position: Vector2 = Vector2(0f, 0f),
    open var scale: Vector2 = Vector2(0f, 0f),
    open var rotation: Float = 0f,
    factory: Behavior.Factory<Node2D, Behavior<Node2D>>? = null,
) {
    private val script: Behavior<Node2D>? = factory?.create(this)

    // Hierarchy
    open var parent: Node2D? = null
    open val children: MutableMap<String, Node2D> = mutableMapOf()

    val globalPosition: Vector2
        get() =
            position + (
                parent?.globalPosition ?: Vector2.Zero
            )

    fun addChild(child: Node2D) {
        child.parent = this
        check(child.name !in children) {
            "Trying to insert child with duplicated ID: ${child.name}"
        }
        children[child.name] = child
    }

    fun removeChild(childId: String) {
        children.remove(childId)
    }

    fun onReady() {
        children.values.forEach { it.onReady() }
        script?.ready() // Called last - let children setup before parent
    }

    fun update(delta: Float) {
        children.values.forEach { it.update(delta) }
        script?.update(delta)
    }

    fun getNode(path: String): Node2D? {
        val parts = path.split("/")
        var current: Node2D = this

        for (part in parts) {
            current = current.children[part] ?: return null
        }

        return current
    }
}

@Suppress("ktlint:standard:function-naming")
fun NodeBuilder.Node(
    name: String,
    script: Behavior.Factory<Node2D, Behavior<Node2D>>? = null,
    position: Vector2 = Vector2(0f, 0f),
    scale: Vector2 = Vector2(0f, 0f),
    rotation: Float = 0f,
    block: NodeBuilder.() -> Unit = {},
) = add(
    Node2D(
        name = name,
        factory = script,
        position = position,
        scale = scale,
        rotation = rotation,
    ),
    block,
)
