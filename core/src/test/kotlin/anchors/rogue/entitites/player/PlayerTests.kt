package anchors.rogue.entitites.player

import canopy.engine.core.managers.ManagersRegistry
import canopy.engine.core.managers.SceneManager
import org.junit.jupiter.api.BeforeAll

class PlayerTests {
    companion object {
        @BeforeAll
        @JvmStatic
        fun setup() {
            ManagersRegistry.register(SceneManager())
        }
    }
}
