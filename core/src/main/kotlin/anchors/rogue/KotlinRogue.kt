package anchors.rogue

import anchors.rogue.screens.DemoScreen
import canopy.engine.app.desktop.DesktopCanopyApp
import canopy.engine.app.desktop.DesktopCanopyAppConfig
import canopy.engine.core.managers.SceneManager
import canopy.engine.physics.systems.PhysicsSystem

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

    val game = DesktopCanopyApp(
        sceneManager,
        config = DesktopCanopyAppConfig(
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
