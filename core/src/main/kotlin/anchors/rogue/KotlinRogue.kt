package anchors.rogue

import anchors.rogue.screens.DemoScreen
import canopy.core.managers.SceneManager
import canopy.graphics.backends.GuiCanopyGame
import canopy.graphics.systems.RenderSystem
import canopy.physics.systems.PhysicsSystem

const val GAME_WIDTH = 1280F
const val GAME_HEIGHT = 720F

fun main(args: Array<String>) {
    val sceneManager = SceneManager {
        // Register global systems here
        registerSystem(PhysicsSystem())
        registerSystem(RenderSystem(GAME_WIDTH, GAME_HEIGHT))
        // InputSystem(*InputConfig.getInputMappings())
        // AnimationSystem()
    }

    val game = GuiCanopyGame(
        sceneManager,
        onCreate = {
            // it.addScreen(DemoScreen())
            // it.setScreen<DemoScreen>()
        }
    )

    game.launch()
}
