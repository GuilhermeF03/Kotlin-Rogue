package anchors.rogue.shared.utils.nodes

import kotlin.reflect.KClass

class GlobalNodeSystem(
    val requiredNodes: List<KClass<Node>> = listOf(),
) : Behavior<Node>()
