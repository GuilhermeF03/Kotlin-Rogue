package anchors.rogue

import anchors.rogue.features.logbook.LogbookManager
import anchors.rogue.features.saving.SaveManager
import anchors.rogue.screens.DemoScreen
import anchors.rogue.shared.ecs.managers.ManagersRegistry
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.async.KtxAsync

class KotlinRogue : KtxGame<KtxScreen>() {
    // ManagersRegistry is a custom class to manage global managers
    // These managers are not tied to the ECS world lifecycle
    // They can be used across different worlds and screens
    private val managersRegistry =
        ManagersRegistry.apply {
            register(LogbookManager())
            register(SaveManager())
        }

    override fun create() {
        KtxAsync.initiate()
        // Register and setup global managers - these are not tied to the ECS world lifecycle
        managersRegistry.setup()
        addScreen(DemoScreen())
        setScreen<DemoScreen>()
    }

    override fun dispose() {
        managersRegistry.teardown()
    }
}
