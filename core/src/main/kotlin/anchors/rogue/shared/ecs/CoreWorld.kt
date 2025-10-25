package anchors.rogue.shared.ecs

import anchors.rogue.shared.ecs.managers.ManagersRegistry
import anchors.rogue.shared.ecs.systems.RenderSystem
import com.badlogic.gdx.scenes.scene2d.Stage
import com.github.quillraven.fleks.IntervalSystem
import com.github.quillraven.fleks.configureWorld

fun coreWorld(
    stage: Stage,
    injectables: List<Any> = listOf(),
    systems: List<IntervalSystem> = listOf(),
) = configureWorld {
    injectables {
        add(ManagersRegistry) // Inject the ManagersRegistry
        add(stage)
        injectables.forEach { add(it) }
    }
    systems {
        add(RenderSystem())
        systems.forEach { add(it) }
    }
}
