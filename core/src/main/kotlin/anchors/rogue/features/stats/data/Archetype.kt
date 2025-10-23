package anchors.rogue.features.stats.data

import anchors.rogue.features.combat.Skill
import anchors.rogue.utils.data.registry.IdEntry

/**
 * Represents a character archetype or class in the game
 * @param name The name of the archetype (e.g., Warrior, Mage)
 * @param level The current level of the archetype, default is 0
 * @param passives A list of passive skills granted by this archetype
 * @param skillTree A list of skills available to this archetype
 */
data class Archetype(
    override val name: String,
    val level: Int = 0,
    val passives: List<Skill>,
    val skillTree: List<Skill>,
) : IdEntry {
    override val domain = "archetype"
}
