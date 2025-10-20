package anchors.rogue.features.logbook.bestiary

import kotlinx.serialization.Serializable

@Serializable
data class BestiaryData(
    val discoveredEntities: List<String> = emptyList()
)
