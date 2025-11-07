package anchors.rogue.systems

import anchors.rogue.shared.utils.nodes.core.GlobalNodeSystem
import anchors.rogue.shared.utils.nodes.core.Node
import anchors.rogue.shared.utils.nodes.core.UpdatePhase
import ktx.log.logger

class TestGlobalSystem : GlobalNodeSystem(phase = UpdatePhase.FrameBeforeScene){
    private val logger = logger<TestGlobalSystem>()

    override fun beforeProcess(delta: Float) {
        logger.info { "System tick" }
    }
}
