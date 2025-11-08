package anchors.rogue.screens

import anchors.rogue.shared.managers.ManagersRegistry
import anchors.rogue.shared.utils.nodes.SceneManager
import anchors.rogue.shared.utils.nodes.types.empty.EmptyNode
import anchors.rogue.shared.utils.nodes.types.visual.Sprite2D
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.Texture.TextureFilter.Linear
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ScreenViewport
import ktx.app.KtxScreen
import ktx.assets.disposeSafely
import ktx.assets.toInternalFile
import ktx.log.logger

class DemoScreen : KtxScreen {
    val stage = Stage(ScreenViewport())

    // private val world: World = coreWorld(stage)
    private val logger = logger<DemoScreen>()

    private val sceneManager = ManagersRegistry.get(SceneManager::class)

    // Screen resources
    val image =
        Texture("logo.png".toInternalFile(), true).apply {
            setFilter(Linear, Linear)
        }

    // Entities

    override fun show() {
        logger.info { "Show" }

        sceneManager.currScene =
            EmptyNode("root") {
                Sprite2D(
                    "logo-1",
                    position = Vector2(100F, 200F),
                    texture = image,
                )
                Sprite2D("logo-2", texture = image)
            }
    }

    // Similar to "onUpdate" in other engines
    override fun render(delta: Float) {
        super.render(delta)
        sceneManager.tick(delta)
    }

    // Clean up resources when the screen is disposed
    override fun dispose() {
        super.dispose()
        image.disposeSafely()
    }
}
