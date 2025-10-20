package anchors.rogue.features.logbook.bestiary

import anchors.rogue.utils.data.registry.IdRegistry
import anchors.rogue.utils.signals.createSignal
import com.badlogic.gdx.Gdx

const val BESTIARY_JSON = "data/bestiary.json"

/**
 * A collection of discovered entities in the game.
 * @param discoveredEntities List of entities the player has encountered.
 * @param totalEntities Total number of unique entities in the game.
 */
class Bestiary (
    registryProvider : () -> IdRegistry<BestiaryEntry> = {
        IdRegistry<BestiaryEntry>(Gdx.files.internal(BESTIARY_JSON)).also { it.loadRegistry() }
    }
){
    private val registry = registryProvider()
    // Data
    val discovered: MutableList<BestiaryEntry> = mutableListOf()
    // Total number of unique entities in the game
    val total: Int = registry.nEntries()

    // Signals
    val onDiscovered = createSignal<BestiaryEntry>()

    /**
     * Loads bestiary data from the provided BestiaryData object.
     * This method populates the bestiary with discovered entities.
     * Used when loading a saved game.
     * @param data The BestiaryData object containing the bestiary information to load.
     */
    fun loadData(data: BestiaryData) {
        discovered.clear()
        discovered.addAll(registry.mapIds(data.discovered))
    }

    /**
     * Add new bestiary entry
     */
    fun addEntry(entry : BestiaryEntry){
        discovered.add(entry)
        onDiscovered.emit(entry)
    }
}
