package anchors.rogue.features.logbook.bestiary.data

import kotlinx.serialization.Serializable

/**
 * Data parsed from save file - ids correspond to bestiary entries
 * @see BestiaryEntry
 */
@Serializable
data class BestiarySaveData(
    val discovered: List<String> = emptyList(),
)

fun Bestiary.asData() = BestiarySaveData(this.discovered.map { it.id })
