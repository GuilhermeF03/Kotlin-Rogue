package anchors.rogue.entitites.player

import canopy.core.managers.ManagersRegistry
import canopy.core.nodes.SceneManager
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
