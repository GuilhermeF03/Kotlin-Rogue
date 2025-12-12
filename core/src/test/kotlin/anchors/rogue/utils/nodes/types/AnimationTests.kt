package anchors.rogue.utils.nodes.types

import anchors.rogue.shared.managers.ManagersRegistry
import anchors.rogue.shared.systems.AnimationSystem
import anchors.rogue.shared.utils.nodes.SceneManager
import anchors.rogue.shared.utils.nodes.types.empty.EmptyNode
import anchors.rogue.shared.utils.nodes.types.visual.animation.Animation
import anchors.rogue.shared.utils.nodes.types.visual.animation.AnimationPlayer
import anchors.rogue.shared.utils.nodes.types.visual.animation.tracks.ActionTrack
import anchors.rogue.shared.utils.nodes.types.visual.animation.tracks.PropertyTrack
import com.badlogic.gdx.math.Vector2
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertEquals

class AnimationTests {
    val sceneManager by lazy { ManagersRegistry.get(SceneManager::class) }

    companion object {
        @BeforeAll
        @JvmStatic
        fun setup() {
            ManagersRegistry.register(
                SceneManager {
                    AnimationSystem()
                },
            )
        }
    }

    @Test
    fun `animation structure should work`() {
        val emptyNode = EmptyNode("node")

        val animation =
            Animation("anim", 1.5f) {
                PropertyTrack(emptyNode::position) {
                    key(0.5f, Vector2(0f, 0f))
                }
            }

        assertEquals(1, animation.tracks.size)
        assertEquals(1, animation.tracks[0].keys.size)
    }

    @Test
    fun `test animation`() {
        val emptyNode =
            EmptyNode("node") {
                AnimationPlayer("player")
            }
        var counter = 0

        val animation =
            Animation("anim", 1.5f, false) {
                ActionTrack {
                    key(0.5f) { counter++ }
                    key(1f) { counter++ }
                }
            }

        val animationPlayer = emptyNode.getNode<AnimationPlayer>("player")
        animationPlayer.addAnimation(animation)
        animationPlayer.play(animation.name)

        assertEquals(1, animation.tracks.size)
        assertEquals(2, animation.tracks[0].keys.size)

        sceneManager.currScene = emptyNode

        // Timed action
        var elapsed = 0f
        val delta = 0.1f
        while (elapsed < animation.length) {
            sceneManager.tick(delta)
            elapsed += delta

            Thread.sleep((delta * 100).toLong())
        }
        assertEquals(2, counter)
    }

    @Test
    fun `test adding key outside of defined length`() {
        assertThrows<IllegalArgumentException> {
            Animation("anim", 1.5f) {
                ActionTrack {
                    key(3f) {}
                }
            }
        }
    }
}
