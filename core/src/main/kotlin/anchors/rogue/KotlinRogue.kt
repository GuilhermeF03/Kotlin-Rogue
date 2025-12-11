package anchors.rogue

import anchors.rogue.configs.InputConfig
import anchors.rogue.features.logbook.LogbookManager
import anchors.rogue.screens.DemoScreen
import anchors.rogue.shared.managers.ManagersRegistry
import anchors.rogue.shared.systems.RenderSystem
import anchors.rogue.shared.systems.physics.PhysicsSystem
import anchors.rogue.shared.utils.data.assets.AssetsManager
import anchors.rogue.shared.utils.input.InputSystem
import anchors.rogue.shared.utils.nodes.SceneManager
import anchors.rogue.shared.utils.saving.SaveManager
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.async.KtxAsync

const val GAME_WIDTH = 1280F
const val GAME_HEIGHT = 720F

class KotlinRogue : KtxGame<KtxScreen>() {
    override fun create() {
        KtxAsync.initiate()

        // Register global systems here

        ManagersRegistry.apply {
            register(AssetsManager())
            register(SaveManager())
            register(LogbookManager())

            val sceneManager =
                SceneManager {
                    addSystem(RenderSystem(GAME_WIDTH, GAME_HEIGHT))
                    addSystem(PhysicsSystem())
                    addSystem(InputSystem(*InputConfig.getInputMappings()))
                }
            register(sceneManager)
        }

        // Register and setup global managers - global access to data across screens and nodes
        ManagersRegistry.setup()
        addScreen(DemoScreen())
        setScreen<DemoScreen>()
    }

    override fun resize(
        width: Int,
        height: Int,
    ) {
        ManagersRegistry.get(SceneManager::class).resize(width, height)
    }

    override fun dispose() {
        ManagersRegistry.teardown()
    }
}
