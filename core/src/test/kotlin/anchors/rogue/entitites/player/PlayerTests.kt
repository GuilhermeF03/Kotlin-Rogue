package anchors.rogue.entitites.player

import anchors.rogue.shared.managers.ManagersRegistry
import anchors.rogue.shared.utils.nodes.SceneManager
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
