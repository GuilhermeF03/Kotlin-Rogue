package anchors.rogue

import anchors.rogue.features.logbook.LogbookManager
import anchors.rogue.features.saving.SaveManager
import anchors.rogue.screens.DemoScreen
import anchors.rogue.shared.managers.ManagersRegistry
import anchors.rogue.shared.utils.nodes.SceneManager
import anchors.rogue.systems.RenderSystem
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.async.KtxAsync

const val GAME_WIDTH = 1280F
const val GAME_HEIGHT = 720F

class KotlinRogue : KtxGame<KtxScreen>() {
    override fun create() {
        KtxAsync.initiate()

        // Register global systems here
        val sceneManager =
            SceneManager {
                addSystem(RenderSystem(GAME_WIDTH, GAME_HEIGHT))
            }

        ManagersRegistry.apply {
            register(SaveManager())
            register(LogbookManager())
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
