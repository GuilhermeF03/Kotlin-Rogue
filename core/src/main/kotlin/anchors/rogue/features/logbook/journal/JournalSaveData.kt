package anchors.rogue.features.logbook.journal

import kotlinx.serialization.Serializable

@Serializable
data class JournalSaveData(
    val entries: List<String>,
)

fun Journal.asData() = JournalSaveData(this.entries.map { it.id })
