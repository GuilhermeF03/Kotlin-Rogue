package anchors.rogue.systems.inventory

import anchors.rogue.entities.Stats

sealed class Item(open val name: String, open val stats: Stats, open val rank: Int)

data class Weapon(
    val damage: Int,
    val speed: Int,
    val range: Int,
    override val name: String,
    override val stats: Stats,
    override val rank: Int,
) : Item(name, stats, rank)

data class Armor(
    val defense: Int,
    // val slot: ArmorSlot, <- bot generated
    override val name: String,
    override val stats: Stats,
    override val rank: Int,
) : Item(name, stats, rank)

data class Consumable(
    val effect: String,
    override val name: String,
    override val stats: Stats,
    override val rank: Int,
) : Item(name, stats, rank)
