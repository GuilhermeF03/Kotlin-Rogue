package anchors.rogue.entities

import anchors.rogue.data.serializers.UUIDSerializer
import anchors.rogue.features.combat.Skill
import kotlinx.serialization.Serializable
import java.util.*

/**
 * Represents a character race in the game
 * @param id A unique identifier for the race
 * @param name The name of the race (e.g., Human, Elf)
 * @param stats The base stats associated
 * @param blessing A skill that provides a beneficial effect to characters of this race
 * @param curse A skill that imposes a drawback or limitation on characters of this race
 * @param description A textual description of the race
 */
@Serializable
data class Race(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val stats: Stats,
    val blessing: Skill,
    val curse: Skill,
    val description: String
)
