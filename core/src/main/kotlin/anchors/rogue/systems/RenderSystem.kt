package anchors.rogue.systems

import anchors.rogue.shared.utils.nodes.core.GlobalNodeSystem
import anchors.rogue.shared.utils.nodes.core.UpdatePhase
import anchors.rogue.shared.utils.nodes.types.visual.Sprite2D
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.viewport.FitViewport
import ktx.graphics.use
import ktx.log.logger

/**
 * Renders all Sprite2D nodes using the Scene's active Camera2D.
 */
class RenderSystem(
    worldWidth: Float,
    worldHeight: Float,
) : GlobalNodeSystem(
        UpdatePhase.FrameAfterScene,
        Sprite2D::class,
    ) {
    private val batch = SpriteBatch()
    private val viewport = FitViewport(worldWidth, worldHeight)
    private val log = logger<RenderSystem>()

    override fun onSystemInit() {
        super.onSystemInit()
        viewport.apply()
        sceneManager.onResize.connect(::resize)
    }

    override fun afterProcess(delta: Float) {
        val camera =
            sceneManager.activeCamera?.camera
                ?: return // No camera → skip rendering

        camera.update()
        viewport.camera = camera

        batch.projectionMatrix = camera.combined

        batch.use { b ->
            matchingNodes
                .map { it as Sprite2D }
                .sortedByDescending { it.position.y }
                .forEach { sprite ->
                    val pos = sprite.globalPosition
                    val tex = sprite.texture
                    b.draw(
                        tex,
                        pos.x,
                        pos.y,
                        tex.width.toFloat(),
                        tex.height.toFloat(),
                    )
                }
        }
    }

    private fun resize(
        width: Int,
        height: Int,
    ) {
        log.info { "resize: ${width}x$height" }
        viewport.update(width, height, true)
    }

    override fun onSystemClose() {
        batch.dispose()
    }
}
