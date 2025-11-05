package anchors.rogue.utils.nodes

import anchors.rogue.shared.utils.nodes.Behavior
import anchors.rogue.shared.utils.nodes.Node
import anchors.rogue.shared.utils.nodes.behavior
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.TimeUnit
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertSame
import kotlin.test.assertTrue
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class NodeTests {
    @Test
    fun `structure should pass`() {
        val root = Node("test-node")

        val scene = Node("root") {
            Node("child-a")

            Node("child-b") {
                Node("child-c")
            }
        }

        scene.buildTree()

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
        val behaviour = behavior<Node>(
            onReady = {
                val parent = parent ?: return@behavior
                childCount.merge(parent.name, 1) { old, new -> old + new }
                if (name !in childCount) {
                    childCount[name] = 0
                }
            }
        )

        // Build scene
        Node("Test 2") {
            Node("child-a", behaviour) // pass node

            Node("child-b", behaviour) {
                Node("child-c", behaviour) // pass node
            }
        }.buildTree()

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

    @Test
    fun `ready should execute on correct order`() {
        val callOrder = mutableListOf<String>()
        val behaviour = behavior<Node>(
            onReady = {
                callOrder += name
            }
        )

        // Build scene
        Node("Test 2", behaviour) {
            Node("child-a", behaviour) // pass node

            Node("child-b", behaviour) {
                Node("child-c", behaviour) // pass node
            }
        }.buildTree()

        assertEquals(
            listOf(
                "child-a",
                "child-c",
                "child-b",
                "Test 2",
            ),
            callOrder,
        )
    }

    @Test
    fun `ticks should update state`() = runBlocking {
        var nTicks = 0
        var nPhysicsTicks = 0

        val behavior = behavior<Node>(
            onUpdate = {
                nTicks ++
            },
            onPhysicsUpdate = {
                nPhysicsTicks++
            }
        )

        val tree = Node("root", behavior)
        tree.buildTree()

        launch {
            repeat(2) { i ->
                if (nTicks % (i+1) == 0)
                    tree.physicsUpdate(0f)

                tree.update(0f)
                delay(20.toDuration(DurationUnit.MILLISECONDS))
            }
        }.join()

        assertEquals(2, nTicks)
        assertEquals(1, nPhysicsTicks)
    }

    @Test
    fun `adding should call ready on child node`(){
        var wasCalled = false
        val behavior = behavior<Node>(
            onReady = { wasCalled = true }
        )

        val root = Node("root")
        root.buildTree()

        assertFalse(wasCalled)

        root.addChild(Node("child", behavior))

        assertTrue(wasCalled)
    }

    @Test
    fun `removing node should call onExitTree`(){
        var wasCalled = false
        val behavior = behavior<Node>(
            onExitTree = { wasCalled = true }
        )

        val tree = Node("root"){
            Node("child", behavior)
        }

        assertFalse(wasCalled)
        tree.removeChild("child")

        assertTrue(wasCalled)
    }

    @Test
    fun `queue free should delete node`(){
        val tree = Node("root"){
            Node("child")
        }
        tree.buildTree()

        val child = tree.getNode("child")
        assertNotNull(child)

        child.queueFree()

        assertEquals(0, tree.children.size)
    }



}
