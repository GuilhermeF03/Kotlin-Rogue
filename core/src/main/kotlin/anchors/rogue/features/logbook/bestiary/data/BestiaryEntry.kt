package anchors.rogue.features.logbook.bestiary.data

import anchors.rogue.shared.utils.data.registry.IdEntry
import kotlinx.serialization.Serializable

/**
 * Represents a single entry in the bestiary.
 * @param id Unique identifier for the entity.
 * @param name Name of the entity.
 * @param imagePath Path to the entity's image or icon.
 * @param description A brief description of the entity.
 * @param text Detailed information about the entity, including lore and stats.
 */
@Serializable
data class BestiaryEntry(
    override val name: String,
    val imagePath: String = "",
    val description: String = "",
    val lore: String = "",
) : IdEntry {
    override val domain: String = "beast"
}
