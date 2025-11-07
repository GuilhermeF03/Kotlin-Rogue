package anchors.rogue.systems

import anchors.rogue.shared.utils.nodes.core.GlobalNodeSystem
import anchors.rogue.shared.utils.nodes.core.Node
import anchors.rogue.shared.utils.nodes.core.UpdatePhase
import anchors.rogue.shared.utils.nodes.types.visual.Sprite2D
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ScreenViewport
import ktx.log.logger
import kotlin.math.ceil

class RenderSystem : GlobalNodeSystem(
    phase = UpdatePhase.FrameAfterScene,
    Sprite2D::class
) {
    private val stage = Stage(ScreenViewport())

    private val logger = logger<RenderSystem>()

    override fun onSystemInit() {
        // Optional: subscribe to resize events
        stage.viewport.update(Gdx.graphics.width, Gdx.graphics.height, true)
    }

    override fun onNodeAdded(node: Node<*>) {
        val sprite = node as Sprite2D
        stage.addActor(sprite.image)
    }

    override fun onNodeRemoved(node: Node<*>) {
        val sprite = node as Sprite2D
        sprite.image.remove()
    }

    override fun processNode(node: Node<*>, delta: Float) {
        val sprite = node as Sprite2D
        val transform = sprite.position // however you expose position
        sprite.image.setPosition(transform.x, transform.y)

        // Optional Z/Y sort behavior
        sprite.image.zIndex = ((transform.y * 1000) * -1F).coerceAtLeast(0F).toInt()
    }

    override fun afterProcess(delta: Float) {
        logger.info { "Actors info" }
        stage.actors.forEachIndexed { i, it ->
            logger.info { "[$i]: [${it.x}, ${it.y}] - ${it.name}" }
        }
        with(stage){
            viewport.apply()
            act(delta)
            draw()
        }
    }

    override fun onSystemClose() {
        stage.dispose()
    }
}
