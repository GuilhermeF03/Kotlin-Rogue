package anchors.rogue.features.logbook.journal.data

import anchors.rogue.shared.utils.data.registry.IdEntry
import kotlinx.serialization.Serializable

@Serializable
data class JournalEntry(
    override val name: String = "",
    val type: JournalEntryType = JournalEntryType.DEFAULT,
    val lore: String = "",
) : IdEntry {
    override val domain = "journal"
}

enum class JournalEntryType {
    DEFAULT,
}
