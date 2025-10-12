package anchors.rogue.entities

import kotlinx.serialization.Serializable

/**
 * Represents the core attributes of a character in the game.
 * @param vitality A measure of health and endurance
 * @param strength A measure of physical power and damage potential
 * @param constitution A measure of resilience and ability to withstand damage
 * @param dexterity A measure of agility, reflexes, and accuracy
 * @param luck A measure of fortune and chance-based outcomes
 * @param intelligence A measure of mental acuity, knowledge, and magical ability
 */
@Serializable
data class Stats(
    val vitality: Int,
    val strength: Int,
    val constitution: Int,
    val dexterity: Int,
    val luck: Int,
    val intelligence: Int,
) {
    override fun toString(): String = """
        VIT: $vitality
        STR: $strength
        CON: $constitution
        DEX: $dexterity
        LUCK: $luck
        INT: $intelligence
    """.trimIndent()
}
