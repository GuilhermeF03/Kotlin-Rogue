package anchors.rogue.shared.utils.nodes

abstract class Behavior<out T : Node2D>(
    val self: T,
) {
    /** Lifecycle hooks */
    open fun ready() {}

    open fun update(delta: Float) {}

    open fun exitTree() {}

    interface Factory<T : Node2D, B : Behavior<T>> {
        fun create(node: T): B
    }
}

fun <T : Node2D> behavior(block: (Node2D) -> Behavior<T>): Behavior.Factory<T, Behavior<T>> =
    object : Behavior.Factory<T, Behavior<T>> {
        override fun create(node: T): Behavior<T> = block(node)
    }
