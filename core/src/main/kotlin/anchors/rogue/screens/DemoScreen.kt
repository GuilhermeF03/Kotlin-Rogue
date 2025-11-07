package anchors.rogue.screens

import anchors.rogue.shared.ecs.coreWorld
import anchors.rogue.shared.ecs.graphics.components.SpriteComponent
import anchors.rogue.shared.ecs.graphics.entities.sprite2D
import anchors.rogue.shared.ecs.managers.ManagersRegistry
import anchors.rogue.shared.utils.nodes.SceneManager
import anchors.rogue.shared.utils.nodes.types.empty.EmptyNode
import anchors.rogue.shared.utils.nodes.types.visual.Sprite2D
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.Texture.TextureFilter.Linear
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.github.quillraven.fleks.World
import ktx.app.KtxScreen
import ktx.assets.disposeSafely
import ktx.assets.toInternalFile
import ktx.log.logger

class DemoScreen : KtxScreen {
    val stage = Stage(ScreenViewport())
    //private val world: World = coreWorld(stage)
    private val logger = logger<DemoScreen>()

    private val sceneManager = ManagersRegistry.get(SceneManager::class)

    // Screen resources
    val image =
        Texture("logo.png".toInternalFile(), true).apply {
            setFilter(Linear, Linear)
        }

    // Entities
    val renderableLogo = Sprite2D(
        "logo-1",
        position = Vector2(100F, 1005F),
        texture = image
    )
    val a = Sprite2D("a", texture = image)

    // Similar to "onReady"
    override fun show() {
        logger.info { "Show" }

        val tree = EmptyNode("root"){
            addChild(renderableLogo)
            addChild(a)
        }
        sceneManager.currScene = tree
    }

    // Similar to "onUpdate" in other engines
    override fun render(delta: Float) {
        super.render(delta)

        sceneManager.tick(delta)
        //world.update(delta)
    }

    // Clean up resources when the screen is disposed
    override fun dispose() {
        super.dispose()
        image.disposeSafely()
       // world.dispose()
    }
}
