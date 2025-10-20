package anchors.rogue.features.logbook.bestiary

import kotlinx.serialization.Serializable

@Serializable
data class BestiaryData(val discovered: List<String> = emptyList())
