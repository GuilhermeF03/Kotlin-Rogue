package anchors.rogue.shared.utils.nodes

@DslMarker
annotation class SceneDSL

@SceneDSL
class NodeBuilder(
    private val node: Node,
) {
    fun <T : Node> add(
        child: T,
        block: NodeBuilder.() -> Unit = {},
    ) {
        val builder = NodeBuilder(child)
        builder.block()
        node.addChild(child)
    }
}

fun scene(
    root: Node,
    block: NodeBuilder.() -> Unit = {},
): Node {
    val builder = NodeBuilder(root)
    builder.block()
    root.onReady()
    return root
}
