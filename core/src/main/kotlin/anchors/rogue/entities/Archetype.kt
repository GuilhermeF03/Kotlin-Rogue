package anchors.rogue.entities

import anchors.rogue.systems.combat.Skill

data class Archetype(val name: String, val level: Int = 0, val passive: Skill, val skillTree: List<Skill>)
