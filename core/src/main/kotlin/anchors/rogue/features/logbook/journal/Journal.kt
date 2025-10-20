package anchors.rogue.features.logbook.journal

import anchors.rogue.utils.data.registry.IdRegistry
import anchors.rogue.utils.signals.createSignal
import com.badlogic.gdx.Gdx

data class Journal(
    val registryProvider : () -> IdRegistry<JournalEntry> = {
        IdRegistry<JournalEntry>(Gdx.files.internal("data/logbook/journal")).also { it.loadRegistry() }
    }
){
    private val registry = registryProvider()
    val entries = mutableListOf<JournalEntry>()

    val onNewEntry = createSignal<JournalEntry>()

    fun loadData(data : JournalData){
        entries.clear()
        entries += registry.mapIds<JournalEntry>(data.entries)
    }

    fun addEntry(entry : JournalEntry){
        entries += entry
        onNewEntry.emit(entry)
    }
}
