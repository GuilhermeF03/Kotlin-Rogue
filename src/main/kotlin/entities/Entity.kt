package com.anchors.entities

data class Entity(
    val name: String,
    val race: Race,
    val job: Job,
    val skillPoints: Int = 0,
    val equippedSkills: List<Skill> = listOf(),
    val skills: List<Skill> = listOf(),
    val equippedItems: List<Item> = listOf(),
    val inventory: List<Item> = listOf()
) {
    val health: Int
        get() = race.stats.vitality

    val mana: Int
        get() = race.stats.intelligence
}

enum class Race(
    val stats: Stats,
    val blessing: Skill,
    val curse: Skill
) {
    HUMAN(
        Stats(1,1,1,1,1,1),
        Skill("Jack Of All Trades"),
        Skill("Master Of None")
    ),
    ELF(
        Stats(1,1,1,1,1,1),
        Skill("Son Of The Forest"),
        Skill("Spirit Magic")
    ),
    GOBLIN(
        Stats(1,1,1,1,1,1),
        Skill("Scavenger"),
        Skill("Unblessed")
    ),
    ORC(
        Stats(1,1,1,1,1,0),
        Skill("Blessed Body"),
        Skill("Learning Disability")
    ),
    VAMPIRE(
        Stats(1,1,1,1,1,-1),
        Skill("Blood Sucking"),
        Skill("Blood Magic")
    ),
    AUTOMATA(
        Stats(1,1,1,1,-1,-1),
        Skill("Sacred Shell"),
        Skill("Iron Heart")
    )
}

data class Stats(
    val vitality: Int,
    val strength: Int,
    val constitution: Int,
    val dexterity: Int,
    val luck: Int,
    val intelligence: Int
)

data class Skill(val name: String, val description: String = "")

data class Job(val name: String, val level: Int, val skills: List<Skill>)

data class Item(val name: String, val type: ItemType, val stats: Stats, val rank: Int)

enum class ItemType {
    WEAPON,
    ARMOR,
    MISC
}