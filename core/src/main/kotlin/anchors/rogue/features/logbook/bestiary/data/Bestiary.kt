package anchors.rogue.features.logbook.bestiary.data

import anchors.rogue.features.saving.registerSaveModule
import anchors.rogue.shared.utils.data.registry.IdRegistry
import anchors.rogue.shared.utils.signals.createSignal
import com.badlogic.gdx.Gdx

const val BESTIARY_JSON = "data/bestiary.json"

/**
 * A collection of discovered entities in the game.
 * @param discovered List of entities the player has encountered.
 * @param total Total number of unique entities in the game.
 */
class Bestiary(
    private val registry: IdRegistry<BestiaryEntry> =
        IdRegistry<BestiaryEntry>(Gdx.files.internal(BESTIARY_JSON)).also { it.loadRegistry() },
) {
    // Data
    val discovered = mutableListOf<BestiaryEntry>()

    // Total number of unique entities in the game
    val total: Int = registry.nEntries()

    // Signals
    val onDiscovered = createSignal<BestiaryEntry>()

    init {
        registerSaveModule<BestiarySaveData>(
            "bestiary",
            serializer = BestiarySaveData.serializer(),
            onSave = { this.asData() },
            onLoad = ::loadData,
        )
    }

    /**
     * Loads bestiary data from the provided BestiaryData object.
     * This method populates the bestiary with discovered entities.
     * Used when loading a saved game.
     * @param data The BestiaryData object containing the bestiary information to load.
     */
    fun loadData(data: BestiarySaveData) {
        discovered.clear()
        discovered += registry.mapIds(data.discovered)
    }

    /**
     * Add new bestiary entry
     */
    fun addEntry(entry: BestiaryEntry) {
        discovered += entry
        onDiscovered.emit(entry)
    }
}
