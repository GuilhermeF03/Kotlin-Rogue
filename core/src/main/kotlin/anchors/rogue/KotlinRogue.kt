package anchors.rogue

import anchors.rogue.features.logbook.LogbookManager
import anchors.rogue.features.saving.SaveManager
import anchors.rogue.screens.DemoScreen
import anchors.rogue.shared.ecs.managers.ManagersRegistry
import anchors.rogue.shared.utils.nodes.SceneManager
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.async.KtxAsync

class KotlinRogue : KtxGame<KtxScreen>() {
    // ManagersRegistry is a custom class to manage global managers
    // These managers are not tied to the ECS world lifecycle
    // They can be used across different worlds and screens

    override fun create() {
        KtxAsync.initiate()

        ManagersRegistry.apply {
            register(SaveManager())
            register(LogbookManager())
            register(SceneManager())
        }
        // Register and setup global managers - these are not tied to the ECS world lifecycle
        ManagersRegistry.setup()
        addScreen(DemoScreen())
        setScreen<DemoScreen>()
    }

    override fun dispose() {
        ManagersRegistry.teardown()
    }
}
