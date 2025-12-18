package anchors.framework.nodes.types.visual

import anchors.framework.nodes.core.Behavior
import anchors.framework.nodes.core.Node
import anchors.framework.systems.RenderSystem
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import ktx.log.logger

class AnimatedSprite2D(
    name: String,
    // Node specific props
    var texture: Texture,
    var frameWidth: Int = 0,
    var frameHeight: Int = 0,
    // Node base props
    script: (node: AnimatedSprite2D) -> Behavior<AnimatedSprite2D>? = { null },
    position: Vector2 = Vector2.Zero,
    scale: Vector2 = Vector2(1f, 1f),
    rotation: Float = 0f,
    groups: MutableList<String> = mutableListOf(),
    // DSL
    block: Node<*>.() -> Unit = {},
) : Node<AnimatedSprite2D>(
        name,
        script,
        position,
        scale,
        rotation,
        groups,
        block,
    ) {
    val frames get() = cutFrames()
    var currFrame: Sprite? = null
    private val logger = logger<AnimatedSprite2D>()

    init {
        if (!sceneManager.hasSystem(RenderSystem::class)) {
            logger.error {
                """

                The rendering pipeline wasn't setup - no visual elements will be rendered!

                To fix it: register it into a Scene Manager!

                """.trimIndent()
            }
        }
    }

    private fun cutFrames(): Array<TextureRegion?> {
        check(frameWidth > 0) { "Frame Width is negative." }
        check(frameHeight > 0) { "Frame Height is negative." }
        return TextureRegion
            .split(texture, frameWidth, frameHeight)
            .flatten()
            .toTypedArray()
    }
}
