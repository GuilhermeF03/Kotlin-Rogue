package anchors.rogue

import anchors.framework.data.assets.AssetsManager
import anchors.framework.input.InputSystem
import anchors.framework.managers.ManagersRegistry
import anchors.framework.nodes.SceneManager
import anchors.framework.saving.SaveManager
import anchors.framework.systems.AnimationSystem
import anchors.framework.systems.RenderSystem
import anchors.framework.systems.physics.PhysicsSystem
import anchors.rogue.configs.InputConfig
import anchors.rogue.features.logbook.LogbookManager
import anchors.rogue.screens.DemoScreen
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
            register(
                SceneManager {
                    RenderSystem(GAME_WIDTH, GAME_HEIGHT)
                    PhysicsSystem()
                    InputSystem(*InputConfig.getInputMappings())
                    AnimationSystem()
                },
            )
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
