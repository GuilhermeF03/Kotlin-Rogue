package anchors.rogue.entities

import anchors.rogue.systems.combat.Skill

enum class Race(
    val stats: Stats,
    val blessing: Skill,
    val curse: Skill,
) {
    HUMAN(
        Stats(1, 1, 1, 1, 1, 1),
        Skill("Jack Of All Trades"),
        Skill("Master Of None"),
    ),
    ELF(
        Stats(1, 1, 1, 1, 1, 1),
        Skill("Son Of The Forest"),
        Skill("Spirit Magic"),
    ),
    GOBLIN(
        Stats(1, 1, 1, 1, 1, 1),
        Skill("Scavenger"),
        Skill("Unblessed"),
    ),
    ORC(
        Stats(1, 1, 1, 1, 1, 0),
        Skill("Blessed Body"),
        Skill("Learning Disability"),
    ),
    VAMPIRE(
        Stats(1, 1, 1, 1, 1, -1),
        Skill("Blood Sucking"),
        Skill("Blood Magic"),
    ),
    AUTOMATA(
        Stats(1, 1, 1, 1, -1, -1),
        Skill("Sacred Shell"),
        Skill("Iron Heart"),
    ),
}
