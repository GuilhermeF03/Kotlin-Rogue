package anchors.rogue

import anchors.rogue.screens.DemoScreen
import canopy.backends.gui.GuiCanopyGame
import canopy.core.nodes.SceneManager
import canopy.graphics.systems.RenderSystem
import canopy.physics.systems.PhysicsSystem

const val GAME_WIDTH = 1280F
const val GAME_HEIGHT = 720F

// class KotlinRogue : KtxGame<KtxScreen>() {
//    override fun create() {
//        KtxAsync.initiate()
//
//        // Register and setup global managers - global access to data across screens and nodes
//        ManagersRegistry
//            .apply {
//                register(AssetsManager())
//                register(SaveManager())
//                register(LogbookManager())
//                register(
//                    SceneManager {
//                        // Register global systems here
//                        RenderSystem(GAME_WIDTH, GAME_HEIGHT)
//                        PhysicsSystem()
//                        InputSystem(*InputConfig.getInputMappings())
//                        AnimationSystem()
//                    },
//                )
//            }.setup()
//
//        addScreen(DemoScreen())
//        setScreen<DemoScreen>()
//    }
//
//    override fun resize(
//        width: Int,
//        height: Int,
//    ) {
//        ManagersRegistry.get(SceneManager::class).resize(width, height)
//    }
//
//    override fun dispose() {
//        ManagersRegistry.teardown()
//    }
// }

fun main(args: Array<String>) {
    val sceneManager =
        SceneManager {
            // Register global systems here
            RenderSystem(GAME_WIDTH, GAME_HEIGHT)
            PhysicsSystem()
            // InputSystem(*InputConfig.getInputMappings())
            // AnimationSystem()
        }

    val game =
        GuiCanopyGame(
            sceneManager,
            onCreate = {
                it.addScreen(DemoScreen())
                it.setScreen<DemoScreen>()
            },
        )

    game.launch()
}
