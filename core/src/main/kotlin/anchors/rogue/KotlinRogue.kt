package anchors.rogue

import anchors.rogue.screens.DemoScreen
import io.canopy.engine.app.desktop.DesktopCanopyAppConfig
import io.canopy.engine.app.desktop.desktopApp
import io.canopy.engine.physics.systems.PhysicsSystem

const val GAME_WIDTH = 1280
const val GAME_HEIGHT = 720

fun main(args: Array<String>) = desktopApp {
    sceneManager {
        +PhysicsSystem()
    }

    config(
        DesktopCanopyAppConfig(
            title = "Kotlin Rogue",
            screenWidth = GAME_WIDTH,
            screenHeight = GAME_HEIGHT
        )
    )

    screens {
        start(DemoScreen())
    }
}.launch()
