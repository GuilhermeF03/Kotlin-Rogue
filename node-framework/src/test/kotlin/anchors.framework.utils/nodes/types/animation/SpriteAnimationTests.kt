package anchors.framework.utils.nodes.types.animation

import anchors.framework.managers.ManagersRegistry
import anchors.framework.nodes.SceneManager
import anchors.framework.nodes.types.animation.AnimationPlayer
import anchors.framework.nodes.types.animation.SpriteAnimation
import anchors.framework.nodes.types.animation.tracks.PropertyTrack
import anchors.framework.nodes.types.animation.tracks.SpriteTrack
import anchors.framework.nodes.types.empty.EmptyNode
import anchors.framework.nodes.types.visual.AnimatedSprite2D
import anchors.framework.systems.AnimationSystem
import anchors.framework.systems.RenderSystem
import anchors.framework.utils.headlessApp
import com.badlogic.gdx.graphics.g2d.TextureRegion
import org.junit.jupiter.api.BeforeAll
import kotlin.test.Test

class SpriteAnimationTests {

    companion object {
        @BeforeAll
        @JvmStatic
        fun setup() {
            ManagersRegistry.register(
                SceneManager {
                    //RenderSystem(0f, 0f)
                    AnimationSystem()
                },
            )
        }

        @BeforeAll
        @JvmStatic
        fun setupHeadlessApplication() {
            headlessApp()
        }
    }

    @Test
    fun `should create empty animation`(){
        val emptyNode = EmptyNode("root"){
            AnimatedSprite2D<TextureRegion>("sprite")
            AnimationPlayer("player")
        }

        val animation = SpriteAnimation("animation", arrayOf()){
            val animatedSprite = emptyNode.getNode<AnimatedSprite2D<TextureRegion>>("sprite")

            SpriteTrack(
                animatedSprite,
                0.5f to 1,
                1.0f to 2,
            )
        }

        val animationPlayer = emptyNode.getNode<AnimationPlayer>("player")
        animationPlayer.addAnimation(animation)

        animationPlayer.play("animation")
    }
}
