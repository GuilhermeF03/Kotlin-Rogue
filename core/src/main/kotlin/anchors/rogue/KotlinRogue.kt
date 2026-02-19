package anchors.rogue

import anchors.rogue.screens.DemoScreen
import canopy.core.managers.SceneManager
import canopy.graphics.backends.CanopyDesktopGame
import canopy.graphics.backends.CanopyDesktopGameConfig
import canopy.physics.systems.PhysicsSystem

const val GAME_WIDTH = 1280
const val GAME_HEIGHT = 720

fun main(args: Array<String>) {
    println("java.version=" + System.getProperty("java.version"))
    println("java.home=" + System.getProperty("java.home"))
    val sceneManager = SceneManager {
        // Register global systems here
        registerSystem(PhysicsSystem())
        // InputSystem(*InputConfig.getInputMappings())
        // AnimationSystem()
    }

    val game = CanopyDesktopGame(
        sceneManager,
        config = CanopyDesktopGameConfig(
            title = "Kotlin Rogue",
            screenWidth = GAME_WIDTH,
            screenHeight = GAME_HEIGHT
        ),
        onCreate = {
            it.addScreen(DemoScreen())
            it.setScreen<DemoScreen>()
        }
    )

    game.launch()
}
