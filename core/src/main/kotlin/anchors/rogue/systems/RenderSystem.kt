package anchors.rogue.systems

import anchors.rogue.shared.utils.nodes.core.GlobalNodeSystem
import anchors.rogue.shared.utils.nodes.core.UpdatePhase
import anchors.rogue.shared.utils.nodes.types.visual.Sprite2D
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.viewport.FitViewport
import ktx.graphics.use
import ktx.log.logger

/**
 * System responsible for rendering all Sprite2D nodes
 * @see Sprite2D
 */
class RenderSystem(
    width: Float,
    height: Float,
) : GlobalNodeSystem(
        phase = UpdatePhase.FrameAfterScene,
        Sprite2D::class,
    ) {
    private val batch = SpriteBatch()
    private val camera = OrthographicCamera(width, height)
    private val viewport = FitViewport(width, height, camera)
    private val logger = logger<RenderSystem>()

    override fun onSystemInit() {
        super.onSystemInit()
        viewport.apply()
        sceneManager.onResize.connect(::resize)
    }

    override fun afterProcess(delta: Float) {
        camera.update()

        batch.projectionMatrix = camera.combined
        batch.use { batch ->
            // Y-sort nodes
            val ySortedNodes = matchingNodes.sortedByDescending { node -> (node as Sprite2D).position.y }

            for (node in ySortedNodes) {
                val sprite = node as Sprite2D
                val transform = sprite.globalPosition

                // Draw the sprite (LibGDX texture region)
                sprite.texture.let { region ->
                    batch.draw(
                        region,
                        transform.x,
                        transform.y,
                        region.width.toFloat(),
                        region.height.toFloat(),
                    )
                }
            }
        }
    }

    fun resize(
        width: Int,
        height: Int,
    ) {
        logger.info { "Resize: ${width}x$height" }
        viewport.update(width, height, true)
    }

    override fun onSystemClose() {
        batch.dispose()
    }
}
