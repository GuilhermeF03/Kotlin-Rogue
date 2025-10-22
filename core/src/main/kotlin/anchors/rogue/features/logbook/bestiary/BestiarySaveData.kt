package anchors.rogue.features.logbook.bestiary

import kotlinx.serialization.Serializable

/**
 *
 */
@Serializable
data class BestiarySaveData(val discovered: List<String> = emptyList())

fun Bestiary.asData() = BestiarySaveData(this.discovered.map { it.id })
