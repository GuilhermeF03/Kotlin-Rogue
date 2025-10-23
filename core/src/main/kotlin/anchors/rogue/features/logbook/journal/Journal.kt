package anchors.rogue.features.logbook.journal

import anchors.rogue.features.saving.registerSaveModule
import anchors.rogue.utils.data.registry.IdRegistry
import anchors.rogue.utils.signals.createSignal
import com.badlogic.gdx.Gdx

data class Journal(
    private val registry: IdRegistry<JournalEntry> =
        IdRegistry<JournalEntry>(
            Gdx.files.internal("data/logbook/journal"),
        ).also { it.loadRegistry() },
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
