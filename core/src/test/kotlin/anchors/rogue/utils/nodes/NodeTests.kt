package anchors.rogue.utils.nodes

import anchors.rogue.shared.utils.nodes.Behavior
import anchors.rogue.shared.utils.nodes.Node
import anchors.rogue.shared.utils.nodes.Node2D
import anchors.rogue.shared.utils.nodes.behavior
import anchors.rogue.shared.utils.nodes.scene
import kotlin.collections.mapOf
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame

class NodeTests {
    @Test
    fun `structure should pass`() {
        val root = Node2D("test-node")

        val scene =
            scene(root) {
                Node("child-a")

                Node("child-b") {
                    Node("child-c")
                }
            }

        assertSame(2, scene.children.size)
        assertSame(scene, scene.getNode("child-b")?.parent)
        assertSame(
            scene.getNode("child-b"),
            scene.getNode("child-b/child-c")?.parent,
        )
    }

    @Test
    fun `behavior should work`() {
        val childCount: MutableMap<String, Int> = mutableMapOf()

        // Behavior factory lambda
        val behaviour =
            behavior { node ->
                object : Behavior<Node2D>(node) {
                    override fun ready() {
                        val parent = node.parent ?: return
                        childCount.merge(parent.name, 1) { old, new -> old + new }
                        if (node.name !in childCount) {
                            childCount[node.name] = 0
                        }
                    }
                }
            }
        // Build scene
        scene(Node2D("Test 2")) {
            Node("child-a", behaviour) // pass node

            Node("child-b", behaviour) {
                Node("child-c", behaviour) // pass node
            }
        }
        assertEquals(
            mapOf(
                "Test 2" to 2,
                "child-a" to 0,
                "child-b" to 1,
                "child-c" to 0,
            ),
            childCount,
        )
    }
}
