package anchors.rogue

import anchors.rogue.screens.DemoScreen
import io.canopy.engine.app.desktop.DesktopCanopyApp
import io.canopy.engine.app.desktop.DesktopCanopyAppConfig
import io.canopy.engine.core.managers.SceneManager
import io.canopy.engine.physics.systems.PhysicsSystem

const val GAME_WIDTH = 1280
const val GAME_HEIGHT = 720

fun main(args: Array<String>) {
    DesktopCanopyApp(
        sceneManager = SceneManager {
            registerSystem(PhysicsSystem())
        },
        config = DesktopCanopyAppConfig(
            title = "Kotlin Rogue",
            screenWidth = GAME_WIDTH,
            screenHeight = GAME_HEIGHT
        ),
        onCreate = {
            it.addScreen(DemoScreen())
            it.setScreen<DemoScreen>()
        }
    ).launch()
}
