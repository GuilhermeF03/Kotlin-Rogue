package anchors.rogue.features.logbook.bestiary

import kotlinx.serialization.Serializable

/**
 * Represents a single entry in the bestiary.
 * @param id Unique identifier for the entity.
 * @param name Name of the entity.
 * @param picturePath Path to the entity's image or icon.
 * @param description A brief description of the entity.
 * @param text Detailed information about the entity, including lore and stats.
 */
@Serializable
data class BestiaryEntry(
    val id : String,
    val name: String,
    val picturePath: String,
    val description: String,
    val text : String
)
