package anchors.rogue.features.logbook.journal

import anchors.rogue.utils.data.registry.IdEntry
import kotlinx.serialization.Serializable

@Serializable
data class JournalEntry(
    override val name: String = "",
    val type : JournalEntryType = JournalEntryType.DEFAULT,
    val lore: String = ""
) : IdEntry { override val domain = "journal"}

enum class JournalEntryType {
    DEFAULT
}
