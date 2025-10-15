package anchors.rogue.features.logbook.bestiary

/**
 * A collection of discovered entities in the game.
 * @param discoveredEntities List of entities the player has encountered.
 * @param totalEntities Total number of unique entities in the game.
 */
data class Bestiary(val discoveredEntities: List<BestiaryEntry> = listOf(), val totalEntities: Int = 0)
