package anchors.rogue.features.logbook.journal

import kotlinx.serialization.Serializable

@Serializable
data class JournalData(val entries : List<String>)
