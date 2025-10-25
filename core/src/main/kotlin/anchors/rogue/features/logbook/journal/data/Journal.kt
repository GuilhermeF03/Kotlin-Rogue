package anchors.rogue.features.logbook.journal.data

import anchors.rogue.features.saving.registerSaveModule
import anchors.rogue.shared.utils.data.registry.IdRegistry
import anchors.rogue.shared.utils.signals.createSignal
import com.badlogic.gdx.Gdx

const val JOURNAL_LOCATION = "data/logbook/journal/journal.json"

class Journal(
    private val registry: IdRegistry<JournalEntry> =
        IdRegistry<JournalEntry>(
            Gdx.files.internal(JOURNAL_LOCATION),
        ).also { it.loadRegistry<JournalEntry>() },
) {
    // Data
    val entries = mutableListOf<JournalEntry>()

    // Signals
    val onNewEntry = createSignal<JournalEntry>()

    init {
        registerSaveModule<JournalSaveData>(
            id = "journal",
            serializer = JournalSaveData.serializer(),
            onSave = { this.asData() },
            onLoad = ::loadData,
        )
    }

    fun loadData(data: JournalSaveData) {
        entries.clear()
        entries += registry.mapIds<JournalEntry>(data.entries)
    }

    fun addEntry(entry: JournalEntry) {
        entries += entry
        onNewEntry.emit(entry)
    }
}
