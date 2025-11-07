package anchors.rogue

import anchors.rogue.features.logbook.LogbookManager
import anchors.rogue.features.saving.SaveManager
import anchors.rogue.screens.DemoScreen
import anchors.rogue.shared.ecs.managers.ManagersRegistry
import anchors.rogue.shared.utils.nodes.SceneManager
import anchors.rogue.systems.RenderSystem
import anchors.rogue.systems.TestGlobalSystem
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.async.KtxAsync

class KotlinRogue : KtxGame<KtxScreen>() {

    override fun create() {
        KtxAsync.initiate()

        // Register global systems here
        val sceneManager = SceneManager{
            addSystem(TestGlobalSystem())
            addSystem(RenderSystem())
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

    override fun dispose() {
        ManagersRegistry.teardown()
    }
}
