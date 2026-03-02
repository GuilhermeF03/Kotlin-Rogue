package anchors.rogue.screens

import anchors.rogue.entities.player.Player
import com.badlogic.gdx.graphics.Texture.TextureFilter.Linear
import io.canopy.engine.app.core.screen.CanopyScreen
import io.canopy.engine.core.managers.manager
import io.canopy.engine.core.nodes.core.asSceneRoot
import io.canopy.engine.core.nodes.core.treeSystem
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
    // val stage = Stage(ScreenViewport())

    // private val world: World = coreWorld(stage)
    private val logger = logger<DemoScreen>()

    private val assetsManager = manager<AssetsManager>()
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

        treeSystem<PhysicsSystem>().replaceWorld()

        EmptyNode("root") {
            // Player
            Player {
                at(10f, 10f)
            }

            // Static Logo
            StaticBody2D("static-logo-area") {
                at(-200f, 200f)

                Collider2D(
                    "collider",
                    shape = CircleShape2D(50f)
                )
                Sprite2D("logo-2", texture = image)
            }

            StaticBody2D("static-logo-body") {
                at(-400f, 200f)

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
