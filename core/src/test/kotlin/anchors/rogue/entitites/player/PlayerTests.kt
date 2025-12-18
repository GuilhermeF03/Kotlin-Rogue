package anchors.rogue.entitites.player

import anchors.framework.managers.ManagersRegistry
import anchors.framework.nodes.SceneManager
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
