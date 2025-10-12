package anchors.rogue.entities

import anchors.rogue.systems.combat.Skill
import anchors.rogue.systems.inventory.Inventory

data class Entity(
    val name: String,
    val race: Race,
    val archetype: Archetype,
    val skillPoints: Int = 0,
    val equippedSkills: List<Skill> = listOf(),
    val skills: List<Skill> = listOf(),
    val inventory: Inventory,
) {
    val health: Int
        get() = race.stats.vitality

    val mana: Int
        get() = race.stats.intelligence

    val stats: Stats
        get() = race.stats

    val statusWindow: String
        get() = """
            Name: $name
            Race: $race
            Class: ${archetype.name} (Level ${archetype.level})
            Health: $health
            Mana: $mana
            Stats: $stats
            Skill Points: $skillPoints
            Equipped Skills:\n${equippedSkills.joinToString { "- ${it.name}\n" }}
        """.trimIndent()
}
