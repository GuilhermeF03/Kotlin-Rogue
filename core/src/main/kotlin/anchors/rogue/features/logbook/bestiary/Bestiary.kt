package anchors.rogue.features.logbook.bestiary

import java.util.*

/**
 * A collection of discovered entities in the game.
 * @param discoveredEntities List of entities the player has encountered.
 * @param totalEntities Total number of unique entities in the game.
 */
class Bestiary private constructor(
    // Discovered entities mapped by their unique IDs
    val discoveredEntities: Map<UUID, BestiaryEntry> = mutableMapOf(),
    // Total number of unique entities in the game
    val totalEntities: Int = 0
){
    constructor() : this(
        discoveredEntities = mutableMapOf(),
        totalEntities = 0
    )

    /**
     * Loads bestiary data from the provided BestiaryData object.
     * This method populates the bestiary with discovered entities.
     * Used when loading a saved game.
     * @param data The BestiaryData object containing the bestiary information to load.
     */
    fun loadData(data: BestiaryData) {

    }
}
