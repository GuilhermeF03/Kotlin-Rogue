package anchors.rogue.shared.utils.nodes

abstract class Behavior<T : Node>(
    val node: T? = null,
) {
    constructor() : this(null)

    open fun onEnterTree() = Unit

    open fun onReady() = Unit

    open fun onExitTree() = Unit

    open fun onUpdate(delta: Float) = Unit

    open fun onPhysicsUpdate(delta: Float) = Unit

    interface Factory<T : Node, B : Behavior<T>> {
        fun create(node: T): B
    }
}

fun <T : Node> behavior(block: (Node) -> Behavior<T>): Behavior.Factory<T, Behavior<T>> =
    object : Behavior.Factory<T, Behavior<T>> {
        override fun create(node: T): Behavior<T> = block(node)
    }
