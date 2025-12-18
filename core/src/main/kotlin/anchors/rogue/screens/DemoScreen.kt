package anchors.rogue.screens

import anchors.framework.data.assets.AssetsManager
import anchors.framework.data.assets.FileSource
import anchors.framework.managers.ManagersRegistry
import anchors.framework.nodes.SceneManager
import anchors.framework.nodes.types.empty.EmptyNode
import anchors.framework.nodes.types.physics.body.StaticBody2D
import anchors.framework.nodes.types.physics.fixture.Collider2D
import anchors.framework.nodes.types.physics.shape.CircleShape2D
import anchors.framework.nodes.types.visual.Sprite2D
import anchors.framework.systems.physics.PhysicsSystem
import anchors.rogue.entities.player.Player
import com.badlogic.gdx.graphics.Texture.TextureFilter.Linear
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ScreenViewport
import ktx.app.KtxScreen
import ktx.assets.disposeSafely
import ktx.log.logger

class DemoScreen : KtxScreen {
    val stage = Stage(ScreenViewport())

    // private val world: World = coreWorld(stage)
    private val logger = logger<DemoScreen>()

    private val sceneManager = ManagersRegistry.get(SceneManager::class)
    private val assetsManager = ManagersRegistry.get(AssetsManager::class)
    // private val physicsSystem = sceneManager.getSystem(PhysicsSystem::class)

    // Screen resources
    val image =
        assetsManager.loadTexture("logo.png", FileSource.Internal) {
            setFilter(Linear, Linear)
        }

    // Entities

    override fun show() {
        logger.info { "Show" }
        sceneManager.getSystem(PhysicsSystem::class).replaceWorld()

        sceneManager.currScene =
            EmptyNode("root") {
                // Player
                Player(position = Vector2(10f, 10f))

                // Static Logo
                StaticBody2D(
                    "static-logo-area",
                    position = Vector2(-200f, 200f),
                ) {
                    Collider2D(
                        "collider",
                        shape = CircleShape2D(50f),
                    )
                    Sprite2D("logo-2", texture = image)
                }

                StaticBody2D(
                    "static-logo-body",
                    position = Vector2(-400f, 200f),
                ) {
                    Collider2D(
                        "collider",
                        shape = CircleShape2D(100f),
                    )
                    Sprite2D("logo-2", texture = image)
                }
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
