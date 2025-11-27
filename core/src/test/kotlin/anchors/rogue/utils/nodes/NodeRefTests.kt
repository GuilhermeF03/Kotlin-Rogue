package anchors.rogue.utils.nodes

import anchors.rogue.shared.managers.ManagersRegistry
import anchors.rogue.shared.utils.nodes.SceneManager
import anchors.rogue.shared.utils.nodes.core.*
import anchors.rogue.shared.utils.nodes.types.empty.EmptyNode
import org.junit.jupiter.api.BeforeAll
import kotlin.test.Test
import kotlin.test.assertEquals

class NodeRefTests {
    private class NeedsRef(
        name: String,
        script: (node: NeedsRef) -> Behavior<NeedsRef>? = { null },
        val external: NodeRef<*>,
        block: Node<*>.() -> Unit = {},
    ) : Node<NeedsRef>(
            name,
            script,
            block = block,
        )

    companion object {
        @BeforeAll
        @JvmStatic
        fun setup() {
            ManagersRegistry.register(SceneManager())
        }
    }

    @Test
    fun `should reference node`() {
        var referencedNode: String = ""

        val tree =
            EmptyNode(name = "root") {
                NeedsRef(
                    name = "referrer",
                    external = nodeRef("$/external"),
                    script =
                        behavior(
                            onReady = {
                                referencedNode = external.get(this).name
                            },
                        ),
                )
                EmptyNode(name = "external")
            }

        ManagersRegistry.get(SceneManager::class).currScene = tree

        assertEquals("external", referencedNode)
    }
}
