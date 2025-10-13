package anchors.rogue.systems.logbook.bestiary

import anchors.rogue.data.serializers.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.*

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
    @Serializable(with = UUIDSerializer::class)
    val id : UUID,
    val name: String,
    val picturePath: String,
    val description: String,
    val text : String
)
