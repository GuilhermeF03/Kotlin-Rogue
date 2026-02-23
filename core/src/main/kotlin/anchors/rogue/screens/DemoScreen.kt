package anchors.rogue.screens

import anchors.rogue.entities.player.Player
import com.badlogic.gdx.graphics.Texture.TextureFilter.Linear
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ScreenViewport
import io.canopy.engine.app.core.CanopyScreen
import io.canopy.engine.core.managers.ManagersRegistry
import io.canopy.engine.core.nodes.core.asSceneRoot
import io.canopy.engine.core.nodes.types.empty.EmptyNode
import io.canopy.engine.data.core.assets.AssetsManager
import io.canopy.engine.graphics.nodes.visual.Sprite2D
import io.canopy.engine.logging.api.logger
import io.canopy.engine.physics.nodes.body.StaticBody2D
import io.canopy.engine.physics.nodes.fixture.Collider2D
import io.canopy.engine.physics.nodes.shape.CircleShape2D
import io.canopy.engine.physics.systems.PhysicsSystem
import ktx.assets.disposeSafely

class DemoScreen : CanopyScreen() {
    val stage = Stage(ScreenViewport())

    // private val world: World = coreWorld(stage)
    private val logger = logger<DemoScreen>()

    private val assetsManager = ManagersRegistry.get(AssetsManager::class)
    // private val physicsSystem = sceneManager.getSystem(PhysicsSystem::class)

    // Screen resources
    val image =
        assetsManager.loadTexture("logo.png", AssetsManager.FileSource.Internal) {
            setFilter(Linear, Linear)
        }

    // Entities

    override fun setup() {
        super.setup()

        logger.info { "Example info" }
        logger.warn { "Example warn" }
        logger.debug { "Example log" }
        logger.error { "Example error" }
        sceneManager.getSystem(PhysicsSystem::class).replaceWorld()

        EmptyNode("root") {
            // Player
            Player(position = Vector2(10f, 10f))

            // Static Logo
            StaticBody2D(
                "static-logo-area",
                position = Vector2(-200f, 200f)
            ) {
                Collider2D(
                    "collider",
                    shape = CircleShape2D(50f)
                )
                Sprite2D("logo-2", texture = image)
            }

            StaticBody2D(
                "static-logo-body",
                position = Vector2(-400f, 200f)
            ) {
                Collider2D(
                    "collider",
                    shape = CircleShape2D(100f)
                )
                Sprite2D("logo-2", texture = image)
            }
        }.asSceneRoot()

        logger.info { "SceneRoot" }
    }

    // Clean up resources when the screen is disposed
    override fun dispose() {
        super.dispose()
        image.disposeSafely()
    }
}
