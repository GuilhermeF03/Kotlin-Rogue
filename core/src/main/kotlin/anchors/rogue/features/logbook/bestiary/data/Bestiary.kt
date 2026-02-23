package anchors.rogue.features.logbook.bestiary.data

import canopy.engine.core.signals.createSignal
import canopy.engine.data.core.registry.IdRegistry
import canopy.engine.data.saving.registerSaveModule
import com.badlogic.gdx.Gdx
import kotlinx.serialization.builtins.serializer

const val BESTIARY_JSON = "data/logbook/bestiary/bestiary.json"

/**
 * A collection of discovered entities in the game.
 * @param discovered List of entities the player has encountered.
 * @param total Total number of unique entities in the game.
 */
class Bestiary(
    private val registry: IdRegistry<BestiaryEntry> =
        IdRegistry<BestiaryEntry>(
            Gdx.files.internal(BESTIARY_JSON)
        ).also { it.loadRegistry<BestiaryEntry>() },
) {
    val discovered = mutableListOf<BestiaryEntry>()

    // Total number of unique entities in the game
    val total: Int = registry.nEntries()

    // Signals
    val onDiscovered = createSignal<BestiaryEntry>()

    init {
        // Register save module to save bestiary data onto player save file
        registerSaveModule(
            "player",
            "bestiary",
            serializer = BestiarySaveData.serializer(),
            onSave = { asData() },
            onLoad = ::loadData
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
