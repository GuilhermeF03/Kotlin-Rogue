package anchors.rogue.entities

import anchors.rogue.systems.combat.Skill

data class Archetype(val name: String, val level: Int, val passive: Skill, val skillTree: List<Skill>)
