package anchors.rogue.features.logbook.journal.data

import com.badlogic.gdx.Gdx
import io.canopy.engine.core.signals.createSignal
import io.canopy.engine.data.core.registry.IdRegistry
import io.canopy.engine.data.saving.registerSaveModule

const val JOURNAL_LOCATION = "data/logbook/journal/journal.json"

class Journal(
    private val registry: IdRegistry<JournalEntry> =
        IdRegistry<JournalEntry>(
            Gdx.files.internal(JOURNAL_LOCATION)
        ).also { it.loadRegistry<JournalEntry>() },
) {
    // Data
    val entries = mutableListOf<JournalEntry>()

    // Signals
    val onNewEntry = createSignal<JournalEntry>()

    init {
        registerSaveModule(
            "player",
            id = "journal",
            serializer = JournalSaveData.serializer(),
            onSave = { this.asData() },
            onLoad = ::loadData
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
